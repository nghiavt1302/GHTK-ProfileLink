package com.example.ghtkprofilelink.service.impl;

import com.example.ghtkprofilelink.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    private final SpringTemplateEngine templateEngine;

    public MailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendMail(Map<String, Object> props, String mail, String template, String subject) throws MessagingException {
        Context context = new Context();
        context.setVariables(props);
        String html = templateEngine.process(template, context);

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        helper.setTo(mail);
        helper.setSubject(subject);
        helper.setText(html, true);

        mailSender.send(message);
    }
}
