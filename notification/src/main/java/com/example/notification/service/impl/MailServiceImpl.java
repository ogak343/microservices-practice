package com.example.notification.service.impl;

import com.example.notification.config.TemplateConfiguration;
import com.example.notification.contants.Template;
import com.example.notification.dto.NotificationDto;
import com.example.notification.service.NotificationService;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.nio.charset.StandardCharsets;
import java.util.Set;

@Service(value = "MAIL")
public class MailServiceImpl implements NotificationService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final TemplateConfiguration templateConfiguration;

    public MailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine, TemplateConfiguration templateConfiguration) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.templateConfiguration = templateConfiguration;
    }

    @Override
    @SneakyThrows
    public void sendOTP(NotificationDto dto) {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        Context context = new Context();

        validationFields(dto.getTemplate(), dto.getMessage().keySet());
        String htmlContent;
        dto.getMessage().forEach(context::setVariable);

        switch (dto.getTemplate()) {
            case PAYMENT_CREATE -> htmlContent = templateEngine.process("PaymentCreation.html", context);
            case PAYMENT_PERFORM -> htmlContent = templateEngine.process("PaymentPerform.html", context);
            case ACCOUNT_VERIFICATION -> htmlContent = templateEngine.process("AccountVerification.html", context);
            default -> throw new RuntimeException("Invalid template");
        }

        helper.setTo(dto.getReceiver());
        helper.setSubject(dto.getTitle());
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    private void validationFields(Template template, Set<String> set) {

        if (templateConfiguration.getConfig(template).stream().anyMatch(field -> !set.contains(field)))
            throw new RuntimeException("Invalid template");
    }
}
