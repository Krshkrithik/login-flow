package com.example.signup.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
public class LoginDto {
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Not a valid dto")
    private String emailId;
    private String password;
    private String grantType;
    private String refreshToken;
}
