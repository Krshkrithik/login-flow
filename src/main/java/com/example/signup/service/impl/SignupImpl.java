package com.example.signup.service.impl;

import com.example.signup.dto.EmailValidationDto;
import com.example.signup.dto.SignupDto;
import com.example.signup.exceptions.EmailNotValidException;
import com.example.signup.exceptions.PasswordMisMatchException;
import com.example.signup.mapper.SignupMapper;
import com.example.signup.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Service
@Slf4j
public class SignupImpl implements SignupService {

    @Autowired
    SignupMapper mapper;

    @Override
    public void addUser(SignupDto dto) {
        Optional<EmailValidationDto> user = mapper.getUserByEmail(dto.getEmail());
        user.orElseThrow(EmailNotValidException::new);
        EmailValidationDto email_user = user.get();
        if (email_user.getOtp().equals(dto.getOtp())) {
            if (dto.getPassword().equals(dto.getConformPassword())) {
                dto.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
                dto.setActive(true);
                dto.setUpdatedAt(Timestamp.from(Instant.now()));
                dto.setCreatedAt(Timestamp.from(Instant.now()));
                try {
                    mapper.addUser(dto);
                } catch (Exception e) {
                    log.error("Error in adding User!");
                }
            } else {
                throw new PasswordMisMatchException();
            }
        } else {
            throw new EmailNotValidException();
        }
        log.info(String.valueOf(dto));
    }
}
