package com.security.SpringSecurityAPI.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service("emailService")
public class EmailService {

    Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String message) {
        logger.info("Sending email to {} with subject {} and message {}", to, subject, message);
        
        SimpleMailMessage registrationEmail = new SimpleMailMessage();

        registrationEmail.setTo(to);
        registrationEmail.setSubject(subject);
        registrationEmail.setText(message);

        mailSender.send(registrationEmail);

        logger.info("Email successfully sent to {}", to);
    }
}
