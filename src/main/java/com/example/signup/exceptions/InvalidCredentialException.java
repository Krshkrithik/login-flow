package com.example.signup.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvalidCredentialException extends RuntimeException{
    private int errorStatus;
    public String erroMessage;

}
