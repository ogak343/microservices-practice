package com.example.notification.config;

import com.example.notification.dto.EmailReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

public class CustomDtoDeserializer implements Deserializer<EmailReqDto> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public EmailReqDto deserialize(String topic, byte[] data) {
        try {

            return objectMapper.readValue(data, EmailReqDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing JSON to NotificationDto", e);
        }
    }
}
