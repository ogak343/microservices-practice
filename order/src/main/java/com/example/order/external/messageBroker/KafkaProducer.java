package com.example.order.external.messageBroker;

import com.example.order.dto.req.OrderUpdate;
import com.example.order.external.messageBroker.dto.OrderCreatePublishDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {

    @Value("${kafka.topic.order.create}")
    private String ORDER_CREATION_TOPIC;

    @Value("${kafka.topic.order.modify}")
    private String ORDER_MODIFICATION_TOPIC;

    private final KafkaTemplate<String, Object> kafkaTemplate;


    public void publishOrderCreation(OrderCreatePublishDto dto) {

        kafkaTemplate.send(ORDER_CREATION_TOPIC, dto);
    }

    public void publishOrderModification(OrderUpdate dto) {

        kafkaTemplate.send(ORDER_MODIFICATION_TOPIC, dto);
    }
}
