package com.example.signup.service;

import com.example.signup.dto.LoginDto;
import com.example.signup.dto.LoginResponse;

public interface LoginService {

    LoginResponse login(LoginDto dto);
}
