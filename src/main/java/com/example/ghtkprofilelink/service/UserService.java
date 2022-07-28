package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.constants.ProviderEnum;
import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.model.dto.UserRegister;
import com.example.ghtkprofilelink.model.entity.UserEntity;
import com.example.ghtkprofilelink.model.response.Data;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface UserService {
//    ListData getAll(int page, int pageSize);

    Data getById(Long id);

    Data getByUsername(String username);

    Data add(UserDto userDto);

    Data update(UserDto userDto);

    Data deleteById(Long id);

    Data deleteByUsername(String username);

    Data register(UserRegister userRegister, StringBuffer siteURL) throws UnsupportedEncodingException, MessagingException;

    Data verify(String verificationCode);

    Data updatePasswordToken(String mail, StringBuffer siteUrl) throws MessagingException;

    Data updatePassword(String code, String password);

    Data forgotPassword(String mail) throws MessagingException;

    UserEntity processOAuthPostLogin(UserEntity userEntity, ProviderEnum provider);
}
