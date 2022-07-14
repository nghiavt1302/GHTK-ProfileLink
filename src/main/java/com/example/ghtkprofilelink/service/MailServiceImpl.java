package com.example.ghtkprofilelink.service;

import com.example.ghtkprofilelink.model.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void sendMail(UserEntity user, String siteUrl, String template, String subject) throws MessagingException {
        Map<String, Object> props = new HashMap<>();
        props.put("name", user.getUsername());
        props.put("url", siteUrl);

        Context context = new Context();
        context.setVariables(props);
        String html = templateEngine.process(template, context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        helper.setTo(user.getMail());
        helper.setSubject(subject);
        helper.setText(html, true);

        mailSender.send(message);
    }
}
