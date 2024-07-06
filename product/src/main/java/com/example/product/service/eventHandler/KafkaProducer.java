package com.example.product.service.eventHandler;

import com.example.product.dto.SaveOrderDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Value("${kafka.topic.save_order}")
    private String TOPIC_SAVE_ORDER;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(SaveOrderDto dto) {
        kafkaTemplate.send(TOPIC_SAVE_ORDER, dto);
    }

}
