package com.example.notification.service;

import com.example.notification.contants.Type;
import com.example.notification.dto.EmailReqDto;
import com.example.notification.dto.SmsReqDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class EvenHandler {

    private final Map<String, NotificationService> notificationService;

    @KafkaListener(topics = {"${kafka.topic.email}"}, groupId = "notification-group")
    public void listen(EmailReqDto dto) {

        notificationService.get(Type.MAIL.name()).sendOTP(dto);
    }


    @KafkaListener(topics = {"${kafka.topic.sms}"}, groupId = "notification-group")
    public void listen(SmsReqDto dto) {

        log.info("Request dto : {}", dto);

        notificationService.get(Type.SMS.name()).sendOTP(dto);
    }

}
