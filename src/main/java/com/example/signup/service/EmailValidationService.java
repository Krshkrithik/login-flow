package com.example.signup.service;

import com.example.signup.dto.EmailValidationDto;
import com.example.signup.dto.VerifyOtp;

public interface EmailValidationService {

    boolean verifyEmail(EmailValidationDto dto);

    void sendOtp(String receiver,String subject,String body);

    String generateOtp();

    String getCurrentTime();

    Boolean isValidOtp(VerifyOtp verifyOtp);

    String getOtpExpireTime(int minutes);

    void resendOtp(EmailValidationDto dto);
}
