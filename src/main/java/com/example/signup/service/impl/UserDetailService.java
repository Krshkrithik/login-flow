package com.example.signup.service.impl;

import com.example.signup.dto.SignupDto;
import com.example.signup.exceptions.BasicException;
import com.example.signup.mapper.UserMapper;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.objects.annotations.Getter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Data
@Component
public class UserDetailService implements UserDetailsService{

    @Autowired
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SignupDto user = userMapper.getUserByEmailAndPassword(username);
        if(user == null){
            throw new BasicException(404,"No user found");
        }

        SimpleGrantedAuthority authorities1 = new SimpleGrantedAuthority("ROLE_LENDER");

        return new User(user.getName(),user.getPassword(), Collections.singleton(authorities1));
    }
}
