package com.example.notification.service.impl;

import com.example.notification.dto.NotificationDto;
import com.example.notification.service.NotificationService;
import org.springframework.stereotype.Service;

@Service(value = "MAIL")
public class MailServiceImpl implements NotificationService {

    @Override
    public void sendOTP(NotificationDto dto) {



    }
}
