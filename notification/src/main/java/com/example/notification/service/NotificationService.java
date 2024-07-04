package com.example.notification.service;

import com.example.notification.dto.NotificationDto;

public interface NotificationService {
    void sendOTP(NotificationDto dto);
}
