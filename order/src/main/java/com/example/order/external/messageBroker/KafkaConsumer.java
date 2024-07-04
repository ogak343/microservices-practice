package com.example.order.external.messageBroker;

import com.example.order.external.messageBroker.dto.OrderCreatePublishDto;
import com.example.order.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final OrderService orderService;

    public KafkaConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = {"${kafka.topic.orderCreation}"}, groupId = "order-service")
    public void listen(OrderCreatePublishDto dto) {
        orderService.save(dto);
    }
}
