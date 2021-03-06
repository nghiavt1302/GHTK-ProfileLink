package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.model.dto.UserDto;
import com.example.ghtkprofilelink.model.dto.UserRegister;
import com.example.ghtkprofilelink.model.entity.UserEntity;
import com.example.ghtkprofilelink.model.response.Data;
import com.example.ghtkprofilelink.model.response.ListData;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public interface UserService {
    ListData getAll(int page, int pageSize);

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

    void processOAuthPostLogin(String username, String email);
}
