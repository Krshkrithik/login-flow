package com.example.signup.exceptions.handler;

import com.example.signup.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.signup.constants.Constants.FAILURE_STATUS;

@RestControllerAdvice
@Slf4j
public class GlobalHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String,String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(e->e.getField(),e->e.getDefaultMessage()));
        log.warn(String.valueOf(errors));
        return ResponseEntity.status(FAILURE_STATUS).body(errors);
    }

    @ExceptionHandler(value = OtpNotSendException.class)
    public ResponseEntity<?> OtpNotSendExceptionHandler(OtpNotSendException ex){
        Map<String,String> response = new HashMap<>();
        response.put("message","Otp has not sent Successfully please retry");
        return ResponseEntity.status(FAILURE_STATUS).body(response);
    }

    @ExceptionHandler(value = OtpMisMatchException.class)
    public ResponseEntity<?> OtpMisMatchExceptionHandler(OtpMisMatchException ex){
        Map<String,String> response = new HashMap<>();
        response.put("message","Otp has been Mismatched");
        return ResponseEntity.status(FAILURE_STATUS).body(response);
    }

    @ExceptionHandler(value = OtpTimeLimitExceedException.class)
    public ResponseEntity<?> OtpTimeLimitExceedException(OtpTimeLimitExceedException ex){
        Map<String,String> response = new HashMap<>();
        response.put("message","Time Limit Exceed Please try again");
        return ResponseEntity.status(FAILURE_STATUS).body(response);
    }

    @ExceptionHandler(value = InvalidCredentialException.class)
    public ResponseEntity<?> InvalidCredentialException(InvalidCredentialException ex){
        Map<String,String> response = new HashMap<>();
        response.put("message","Please provide valid details");
        return ResponseEntity.status(FAILURE_STATUS).body(response);
    }

    @ExceptionHandler(value = EmailNotValidException.class)
    public ResponseEntity<?> EmailNotValidException(EmailNotValidException ex){
        Map<String,String> response = new HashMap<>();
        response.put("message","Please verify your email-id.");
        return ResponseEntity.status(FAILURE_STATUS).body(response);
    }

    @ExceptionHandler(value = PasswordMisMatchException.class)
    public ResponseEntity<?> PasswordMisMatchException(PasswordMisMatchException ex){
        Map<String,String> response = new HashMap<>();
        response.put("message","Password and conform password are mismatched!");
        return ResponseEntity.status(FAILURE_STATUS).body(response);
    }

    @ExceptionHandler(value = BasicException.class)
    public ResponseEntity<?> BasicException(BasicException ex){
        Map<String,String> response = new HashMap<>();
        response.put("message",ex.getErrorMessage());
        return ResponseEntity.status(FAILURE_STATUS).body(response);
    }




}
