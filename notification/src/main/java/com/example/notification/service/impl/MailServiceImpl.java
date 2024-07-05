package com.example.notification.service.impl;

import com.example.notification.dto.NotificationDto;
import com.example.notification.service.NotificationService;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service(value = "MAIL")
public class MailServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    @Value("${mail.subject.otp}")
    private String subject;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @SneakyThrows
    public void sendOTP(NotificationDto dto) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setTo(dto.getReceiver());
        mimeMessageHelper.setText(dto.getMessage());
        mimeMessageHelper.setSubject(subject);
        mailSender.send(mimeMessage);
    }
}
