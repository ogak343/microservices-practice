package com.example.payment.service.helper;

import com.example.payment.dto.EmailReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaPublisher {

    @Value("${kafka.topic.email}")
    private String TOPIC;

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void publishPaymentOtp(EmailReqDto otp) {

        kafkaTemplate.send(TOPIC, otp);
    }
}
