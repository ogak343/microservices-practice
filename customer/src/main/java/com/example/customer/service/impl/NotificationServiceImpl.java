package com.example.customer.service.impl;

import com.example.customer.entity.OTP;
import com.example.customer.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {


    @Override
    public void sendOTP(OTP otp) {

        //TODO implementation for Notification service
        log.info("otp code {}", otp.getCode());

    }
}
