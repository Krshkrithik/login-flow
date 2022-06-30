package com.example.signup.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NotNull
public class PayloadDto {
    private String Subject;
    private String[] roles;
}
