package com.example.signup.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.sql.Timestamp;

@Data
public class EmailValidationDto {

    @Pattern(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$",message = "Email Id is not valid")
    @NotEmpty
    private String emailId;

    @JsonIgnore
    private String otp;

    @JsonIgnore
    private String expiresOn;

    @JsonIgnore
    private Timestamp createdAt;

    @JsonIgnore
    private Timestamp updatedAt;

    private int whatsAppAlert;

}
