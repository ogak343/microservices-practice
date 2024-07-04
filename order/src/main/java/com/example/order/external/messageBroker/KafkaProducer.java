package com.example.order.external.messageBroker;

import com.example.order.external.messageBroker.dto.OrderCreatePublishDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProducer {

    @Value("${kafka.topic.orderCreation}")
    private String ORDER_CREATION_TOPIC;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderCreation(OrderCreatePublishDto dto) {
        kafkaTemplate.send(ORDER_CREATION_TOPIC, dto);
    }
}
