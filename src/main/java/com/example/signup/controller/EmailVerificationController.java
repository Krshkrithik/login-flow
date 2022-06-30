package com.example.signup.controller;

import com.example.signup.dto.EmailValidationDto;
import com.example.signup.dto.VerifyOtp;
import com.example.signup.exceptions.OtpTimeLimitExceedException;
import com.example.signup.service.EmailValidationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class EmailVerificationController {

    @Autowired
    EmailValidationService service;

    @PostMapping("/send-email-otp")
    @ApiOperation(value="SendOtpEmail")
    public ResponseEntity<?> VerifyEmail(@Valid @RequestBody EmailValidationDto dto) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "failed");
        response.put("email", "please retry");
        if (service.verifyEmail(dto)) {
            response.replace("status", "success");
            response.replace("email", "otp has been sent successfully");
        }
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtp otp) {
        Map<String, String> response = new HashMap<>();
        response.put("status", "failed");
        response.put("email", "please enter valid otp");
        if (service.isValidOtp(otp)) {
            response.replace("status", "success");
            response.replace("email", "email has been verified successfully");
        } else
            throw new OtpTimeLimitExceedException();
        return ResponseEntity.status(200).body(response);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestParam("email") String email){
        EmailValidationDto dto = new EmailValidationDto();
        dto.setEmailId(email);
        service.resendOtp(dto);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("email", "otp has been sent successfully");
        return ResponseEntity.status(200).body(response);
    }

}
