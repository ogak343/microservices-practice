package com.example.order.config.helpers;

import com.example.order.config.exception.CustomException;
import com.example.order.contants.ErrorCode;
import com.example.order.dto.req.OrderUpdate;
import com.example.order.external.messageBroker.dto.SaveOrderDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.stereotype.Component;

@Component
public class CustomDtoDeserializer implements Deserializer<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Object deserialize(String topic, byte[] data) {
        try {
            if (topic.equals("topic_save_order")) {
                return objectMapper.readValue(data, SaveOrderDto.class);
            } else if (topic.equals("topic_modify_order")) {
                return objectMapper.readValue(data, OrderUpdate.class);
            } else {
                throw new CustomException(ErrorCode.INVALID_TOPIC);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error deserializing JSON to ObjectClass", e);
        }
    }
}
