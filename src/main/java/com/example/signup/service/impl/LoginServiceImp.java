package com.example.signup.service.impl;

import com.example.signup.dto.LoginDto;
import com.example.signup.dto.LoginResponse;
import com.example.signup.dto.SignupDto;
import com.example.signup.exceptions.BasicException;
import com.example.signup.mapper.UserMapper;
import com.example.signup.security.Jwt;
import com.example.signup.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImp  implements LoginService {

    @Autowired
    UserDetailService userDetailService;

    @Autowired
    UserMapper mapper;

    @Autowired
    PasswordEncoder encoder;

    @Override
    public LoginResponse login(LoginDto dto) {
        if(dto.getGrantType().equalsIgnoreCase("password")) {
            SignupDto dtoUser = mapper.getUserByEmailAndPassword(dto.getEmailId());
            if (dtoUser != null) {
                if (dtoUser.isActive()) {
                    if (encoder.matches(dto.getPassword(), dtoUser.getPassword())) {
                        User user = (User) userDetailService.loadUserByUsername(dto.getEmailId());
                        LoginResponse response = new LoginResponse();
                        response.setUserName(dtoUser.getName());
                        response.setAccessToken(Jwt.generateAccessToken(user, "krithik"));
                        response.setRefreshToken(Jwt.generateAccessToken(user, "krithik"));
                        response.setEmail_id(dtoUser.getEmail());
                        response.setRole(user.getAuthorities().toString());
                        response.setStatus("signup_completed");
                        UsernamePasswordAuthenticationToken authenticationToken  = new UsernamePasswordAuthenticationToken(user.getUsername(),null,user.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        return response;
                    }
                }
                throw new BasicException(404, "Password Mismatch");
            }
            throw new BasicException(404, "User Not Found");
        }
        throw new BasicException(404,"Not a valid grant_type");
    }
}
