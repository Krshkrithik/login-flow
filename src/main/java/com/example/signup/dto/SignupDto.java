package com.example.signup.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

@Data
public class SignupDto {

    @NotNull
    private String name;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Enter valid email")
    private String email;

    @NotNull
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$",message = "Enter a valid mobile number")
    private String mobileNo;

    @NotNull
    @Pattern(regexp = "^[0-9][0-9][0-9][0-9][0-9][0-9]$",message = "Please enter valid otp")
    private String otp;

    @NotNull
    @Pattern(regexp = "^[0-9]*$",message = "Should contains only digits")
    private String walletBalance;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$",message = "Minimum eight characters, " +
            "at least one uppercase letter, one lowercase letter and one number")
    private String password;

    private String conformPassword;

    @JsonIgnore
    private Timestamp createdAt;

    @JsonIgnore
    private Timestamp updatedAt;

    @JsonIgnore
    private boolean isActive;
}
