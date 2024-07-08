package com.example.product.config;

import com.example.product.dto.request.OrderUpdate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;

public class CustomDtoDeserializer implements Deserializer<OrderUpdate> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public OrderUpdate deserialize(String topic, byte[] data) {
        try {

            return objectMapper.readValue(data, OrderUpdate.class);
        } catch (Exception e) {
            throw new RuntimeException("Error deserializing JSON to OrderUpdate", e);
        }
    }
}
