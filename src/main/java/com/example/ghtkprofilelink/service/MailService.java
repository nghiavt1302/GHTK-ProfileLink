package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.model.entity.UserEntity;

import javax.mail.MessagingException;

public interface MailService {
    void sendMail(UserEntity user, String siteUrl, String template, String subject) throws MessagingException;
}
