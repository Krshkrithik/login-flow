package com.example.signup.controller;

import com.example.signup.dto.SignupDto;
import com.example.signup.service.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SignupController {

    @Autowired
    SignupService service;

    @PostMapping("/signup")
    public ResponseEntity<?> addUser(@Valid @RequestBody SignupDto dto){
        service.addUser(dto);
        Map<String,String> response = new HashMap<>();
        response.put("status","200");
        response.put("message",dto.getName()+" user added successfully");
        return ResponseEntity.status(200).body(response);
    }

    @GetMapping("/hi")
    @PreAuthorize("hasRole('ROLE_LENDER')")
    public String hi(@RequestHeader("AUTHORIZATION") String authHeader) {
        return "hi";
    }

    @GetMapping("/hello")
    @PreAuthorize("hasRole('ROLE_LENDER')")
    public String hello(@RequestHeader("AUTHORIZATION") String authHeader) {
        return "hi";
    }
}
