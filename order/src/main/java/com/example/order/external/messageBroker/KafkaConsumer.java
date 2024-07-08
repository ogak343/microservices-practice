package com.example.order.external.messageBroker;

import com.example.order.dto.req.OrderUpdate;
import com.example.order.external.messageBroker.dto.SaveOrderDto;
import com.example.order.service.impl.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

    private final OrderServiceImpl orderService;

    public KafkaConsumer(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = {"${kafka.topic.save_order}"}, groupId = "order-service")
    public void listen(SaveOrderDto dto) {

        log.info("Received save order: {}", dto);
        orderService.save(dto);
    }

    @KafkaListener(topics = {"${kafka.topic.modify_order}"}, groupId = "order-service")
    public void listen(OrderUpdate dto) {

        log.info("Received modify order: {}", dto);
        orderService.modify(dto);
    }

}
