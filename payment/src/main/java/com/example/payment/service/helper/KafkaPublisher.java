package com.example.payment.service.helper;

import com.example.payment.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaPublisher {

    @Value("${kafka.topic.payment_otp}")
    private String TOPIC;

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void publishPaymentOtp(NotificationDto otp) {

        kafkaTemplate.send(TOPIC, otp);
    }
}
