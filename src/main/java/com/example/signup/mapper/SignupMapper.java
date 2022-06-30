package com.example.signup.mapper;


import com.example.signup.dto.EmailValidationDto;
import com.example.signup.dto.SignupDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Optional;

@Mapper
public interface SignupMapper {

    @Insert("insert into verify_email (email_id,otp,expires_on,created_at,updated_at)" +
            "values (#{emailId},#{otp},#{expiresOn},#{createdAt},#{updatedAt})")
    void verifyEmail(EmailValidationDto dto);

    @Select("Select * from verify_email where email_id=#{email}")
    Optional<EmailValidationDto> getUserByEmail(String email);


    @Insert("Insert into user_details (name,email,mobile_no,password,created_at,updated_at,is_active,wallet_balance)" +
            "values (#{name},#{email},#{mobileNo},#{password},#{createdAt},#{updatedAt},#{isActive},#{walletBalance})")
    void addUser(SignupDto dto);

    @Select("Select * from user_details where email=#{email}")
    Optional<SignupDto> checkUserByEmail(String email);

    @Update("update verify_email set otp=#{otp},expires_on=#{expiresOn},updated_at=#{updatedAt} where email_id=#{emailId}")
    void updateOtp(EmailValidationDto dto);

}
