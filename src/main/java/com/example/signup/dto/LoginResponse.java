package com.example.signup.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String userName;
    private String accessToken;
    private String refreshToken;
    private String role;
    private String email_id;
    private String status;
}
