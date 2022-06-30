package com.example.signup.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OtpTimeLimitExceedException extends RuntimeException{
    public int errorStatus;
    public String errorMessage;
}
