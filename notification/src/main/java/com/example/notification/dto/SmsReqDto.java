package com.example.notification.dto;

import java.util.Objects;

public record SmsReqDto(
        String title,
        String message,
        String receiver
) implements NotificationDto {

    public boolean isValid() {
        return Objects.nonNull(title) && Objects.nonNull(message) && Objects.nonNull(receiver);
    }
}
