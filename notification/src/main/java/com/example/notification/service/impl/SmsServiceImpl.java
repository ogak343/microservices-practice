package com.example.notification.service.impl;

import com.example.notification.dto.NotificationDto;
import com.example.notification.dto.SmsReqDto;
import com.example.notification.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service(value = "SMS")
public class SmsServiceImpl implements NotificationService {

    @Override
    public void sendOTP(NotificationDto req) {
        if (!(req instanceof SmsReqDto dto) || !dto.isValid()) {
            throw new RuntimeException("Invalid request");
        }
    }
}
