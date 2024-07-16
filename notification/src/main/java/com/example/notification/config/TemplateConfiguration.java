package com.example.notification.config;

import com.example.notification.contants.Template;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@ConfigurationProperties(prefix = "template")
@Configuration
@Data
public class TemplateConfiguration {
    private List<String> accountVerification;
    private List<String> paymentCreation;
    private List<String> paymentPerform;

    public List<String> getConfig(Template template) {
        return switch (template) {
            case ACCOUNT_VERIFICATION -> accountVerification;
            case PAYMENT_CREATE -> paymentCreation;
            case PAYMENT_PERFORM -> paymentPerform;
        };
    }
}
