package com.example.notification.service;

import com.example.notification.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvenHandler {

    private final Map<String, NotificationService> notificationService;

    @KafkaListener(topics = {"${kafka.topic.otp}"}, groupId = "notification-group")
    public void listen(NotificationDto dto) {

        validateDto(dto);
        log.info("Request dto : {}", dto);

        notificationService.get(dto.getType().name()).sendOTP(dto);
    }

    private void validateDto(NotificationDto dto) {
        if (!dto.isValid()) {
            throw new RuntimeException("Invalid dto");
        }
    }
}
