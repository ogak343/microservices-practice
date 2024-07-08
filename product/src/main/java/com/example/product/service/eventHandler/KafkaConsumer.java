package com.example.product.service.eventHandler;

import com.example.product.dto.request.OrderUpdate;
import com.example.product.service.impl.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

    private final ProductServiceImpl productService;

    public KafkaConsumer(ProductServiceImpl productService) {
        this.productService = productService;
    }

    @KafkaListener(topics = {"${kafka.topic.modify_product}"}, groupId = "product-service")
    public void listen(OrderUpdate dto) {

        log.info("Received order update: {}", dto);
        productService.modify(dto);
    }
}
