package com.example.notification.config;

import com.example.notification.dto.NotificationDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

public class CustomDtoDeserializer implements Deserializer<NotificationDto> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public NotificationDto deserialize(String topic, byte[] data) {
        try {

            return objectMapper.readValue(data, NotificationDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing JSON to NotificationDto", e);
        }
    }
}
