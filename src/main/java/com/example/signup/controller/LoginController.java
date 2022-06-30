package com.example.signup.controller;

import com.example.signup.dto.LoginDto;
import com.example.signup.dto.LoginResponse;
import com.example.signup.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class LoginController {

    @Autowired
    LoginService loginService;


    @PostMapping("/login")
    public void login(
            @RequestParam("emailId") String email, @RequestParam("password") String password){

    }

//    @PostMapping("/authenticate")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto){
//        LoginResponse LoginResponse = loginService.login(loginDto);
//        return ResponseEntity.status(200).body(LoginResponse);
//    }

}
