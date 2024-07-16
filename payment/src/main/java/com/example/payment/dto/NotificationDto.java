package com.example.payment.dto;

import com.example.payment.constants.Template;
import com.example.payment.constants.Type;
import lombok.Builder;

@Builder
public record NotificationDto(
        Type type,
        boolean withAttachment,
        Template template,
        String receiver,
        String message) {
}
