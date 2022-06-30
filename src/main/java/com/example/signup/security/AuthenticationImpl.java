package com.example.signup.security;

import com.example.signup.dto.SignupDto;
import com.example.signup.mapper.UserMapper;
import com.example.signup.service.impl.CustomerUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.common.exceptions.InsufficientScopeException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AuthenticationImpl implements AuthenticationProvider {

    @Autowired
    UserMapper mapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();

        String password = authentication.getCredentials().toString();
        SignupDto user = mapper.getUserByEmailAndPassword(username);
        if (user != null) {

            if (!user.isActive()) {
                throw new InsufficientScopeException("User not verified");
            }
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_LENDER");
            CustomerUserDetails userDetails = new CustomerUserDetails();
            userDetails.setUid(String.valueOf(user.getMobileNo()));
            userDetails.setRole("ROLE_LENDER");
            userDetails.setAuthorities(Collections.singletonList(authority));
            return new UsernamePasswordAuthenticationToken(userDetails, authentication.getCredentials(), Collections.singletonList(authority));
        }

        throw new BadCredentialsException("External system authentication failed");
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }
}
