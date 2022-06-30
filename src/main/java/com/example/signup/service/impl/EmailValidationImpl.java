package com.example.signup.service.impl;

import com.example.signup.dto.EmailValidationDto;
import com.example.signup.dto.SignupDto;
import com.example.signup.dto.VerifyOtp;
import com.example.signup.exceptions.*;
import com.example.signup.mapper.SignupMapper;
import com.example.signup.service.EmailValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.example.signup.constants.Constants.*;

@Service
@Slf4j
public class EmailValidationImpl implements EmailValidationService {

    @Autowired
    SignupMapper mapper;

    @Autowired
    private JavaMailSender mailSender;

    private String currentEmailId;

    @Override
    public boolean verifyEmail(EmailValidationDto dto) {

        Optional<SignupDto> user = mapper.checkUserByEmail(dto.getEmailId());
        if(user.isPresent()){
            throw new BasicException(404,"Email id already registered!");
        }


        currentEmailId = dto.getEmailId();
        String otp = generateOtp();

        dto.setOtp(otp);
        dto.setCreatedAt(Timestamp.from(Instant.now()));
        dto.setUpdatedAt(Timestamp.from(Instant.now()));
        dto.setExpiresOn(getOtpExpireTime(5));
        /*try {
            sendOtp(dto.getEmailId(), SUBJECT_OTP, BODY_OTP+otp);
            mapper.verifyEmail(dto);
            log.info(String.valueOf(dto));
            return true;
        }
        catch(Exception e) {
            return false;
        }*/
        mapper.verifyEmail(dto);
        return true;

    }

    @Override
    public void sendOtp(String receiver, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("dummy@gmail.com");
            message.setTo(receiver);
            message.setText(body);
            message.setSubject(subject);

            mailSender.send(message);
        } catch (Exception e) {
            throw new OtpNotSendException(404, "Otp not sent successfully");
        }
    }

    @Override
    public String generateOtp() {
        return String.valueOf((int) (Math.random() * (MAX - MIN + 1) + MIN));
    }

    @Override
    public String getCurrentTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

    @Override
    public Boolean isValidOtp(VerifyOtp verifyOtp) {
        Optional<EmailValidationDto> userDetails = mapper.getUserByEmail(verifyOtp.getEmailId());
        userDetails.orElseThrow(InvalidCredentialException::new);
        EmailValidationDto dto = userDetails.get();
        if (dto.getOtp().equals(verifyOtp.getOtp()))
        {
            SimpleDateFormat simpleDateFormat
                    = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");

            try{
                Date date1 = simpleDateFormat.parse(dto.getExpiresOn());
                Date date2 = simpleDateFormat.parse(getCurrentTime());
                long diff = date1.getTime() - date2.getTime();
                long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
                if(minutes<=10 && minutes>=0){
                    return true;
                }
            }

            catch (Exception e){
                log.warn("Something went wrong in validating otp!");
            }
        }
        else{
            throw new OtpMisMatchException(404,"not a valid otp exception");
        }
        return false;
    }

    @Override
    public String getOtpExpireTime(int minutes) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd-HH:mm:ss");
        LocalDateTime now = LocalDateTime.now().plusMinutes(minutes);
        return dtf.format(now);
    }

    @Override
    public void resendOtp(EmailValidationDto dto){

        String otp = generateOtp();
        dto.setOtp(otp);
        dto.setCreatedAt(Timestamp.from(Instant.now()));
        dto.setUpdatedAt(Timestamp.from(Instant.now()));
        dto.setExpiresOn(getOtpExpireTime(5));
        /*try {
            sendOtp(dto.getEmailId(), SUBJECT_OTP, BODY_OTP+otp);
            mapper.verifyEmail(dto);
            log.info(String.valueOf(dto));
        }
        catch(Exception e) {
            log.warn(e.getMessage());
        }*/
        mapper.updateOtp(dto);
    }
}
