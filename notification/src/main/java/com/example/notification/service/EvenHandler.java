package com.example.notification.service;

import com.example.notification.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EvenHandler {

    private final Map<String, NotificationService> notificationService;

    @KafkaListener(topics = {"${kafka.topic.otp}"}, groupId = "notification")
    public void listen(NotificationDto dto) {

        validateDto(dto);

        notificationService.get(dto.getType().name()).sendOTP(dto);
    }

    private void validateDto(NotificationDto dto) {
        if (Arrays.stream(dto.getClass().getDeclaredFields()).anyMatch(Objects::isNull)) {
            log.error("Invalid dto: {}, missing required fields", dto);
            throw new RuntimeException();
        }
    }
}
