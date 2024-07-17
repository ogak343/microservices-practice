package com.example.notification.dto;

import com.example.notification.contants.Template;

import java.util.Map;
import java.util.Objects;

public record EmailReqDto(
        Template template,
        String title,
        String receiver,
        Map<String, Object> value
) implements NotificationDto {

    public boolean isValid() {
        return Objects.nonNull(value) && Objects.nonNull(template) && Objects.nonNull(title)
                && Objects.nonNull(receiver);
    }
}