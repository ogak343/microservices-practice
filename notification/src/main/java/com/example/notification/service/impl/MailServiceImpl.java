package com.example.notification.service.impl;

import com.example.notification.config.TemplateConfiguration;
import com.example.notification.contants.Template;
import com.example.notification.dto.EmailReqDto;
import com.example.notification.dto.NotificationDto;
import com.example.notification.service.NotificationService;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Set;

@Service(value = "MAIL")
public class MailServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final TemplateConfiguration templateConfiguration;

    public MailServiceImpl(JavaMailSender mailSender,
                           TemplateEngine templateEngine,
                           TemplateConfiguration templateConfiguration) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.templateConfiguration = templateConfiguration;
    }

    @Override
    @SneakyThrows
    public void sendOTP(NotificationDto req) {

        if (!(req instanceof EmailReqDto dto) || !dto.isValid()) {
            throw new RuntimeException("Invalid request");
        }
        validationFields(dto.template(), dto.value().keySet());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        Context context = new Context();

        String htmlContent;
        dto.value().forEach(context::setVariable);

        switch (dto.template()) {
            case ACCOUNT_VERIFICATION -> htmlContent = templateEngine.process("AccountVerification.html", context);
            case PAYMENT_CREATE -> htmlContent = templateEngine.process("PaymentCreation.html", context);
            case PAYMENT_PERFORM -> htmlContent = templateEngine.process("PaymentPerform.html", context);
            default -> throw new RuntimeException("Invalid template");
        }

        helper.setTo(dto.receiver());
        helper.setSubject(dto.title());
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    private void validationFields(Template template, Set<String> set) {

        if (templateConfiguration.getConfig(template).stream().anyMatch(field -> !set.contains(field)))
            throw new RuntimeException("Invalid template");
    }

}
