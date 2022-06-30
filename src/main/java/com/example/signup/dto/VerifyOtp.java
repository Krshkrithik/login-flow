package com.example.signup.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class VerifyOtp {

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Email is not valid")
    @NotNull
    private String emailId;


    @NotNull
    @Pattern(regexp = "^[0-9][0-9][0-9][0-9][0-9][0-9]$",message = "Not a valid Otp!")
    private String Otp;

    @JsonIgnore
    private String receivedTime;


}
