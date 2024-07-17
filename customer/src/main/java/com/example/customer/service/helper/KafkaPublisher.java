package com.example.customer.service.helper;

import com.example.customer.contants.Template;
import com.example.customer.dto.NotificationDto;
import com.example.customer.entity.OTP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class KafkaPublisher {

    @Value("${kafka.topic.email}")
    private String TOPIC_OTP;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendOTP(OTP otp) {

        kafkaTemplate.send(TOPIC_OTP, buildDto(otp));
    }

    private NotificationDto buildDto(OTP otp) {
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setReceiver(otp.getCustomer().getEmail());
        notificationDto.setValue(Map.of("otpCode", otp.getCode()));
        notificationDto.setTitle("AccountVerification");
        notificationDto.setTemplate(Template.ACCOUNT_VERIFICATION);
        return notificationDto;
    }
}
