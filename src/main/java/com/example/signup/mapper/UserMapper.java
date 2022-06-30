package com.example.signup.mapper;

import com.example.signup.dto.LoginDto;
import com.example.signup.dto.SignupDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {


    @Select("select * from user_details where email=#{emailId}")
    SignupDto getUserByEmailAndPassword(String email);
}
