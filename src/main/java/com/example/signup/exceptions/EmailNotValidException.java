package com.example.signup.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailNotValidException extends RuntimeException{
    private int errorStatus;
    private String errorMessage;
}
