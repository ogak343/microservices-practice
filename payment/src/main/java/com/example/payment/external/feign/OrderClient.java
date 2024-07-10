package com.example.payment.external.feign;

import com.example.payment.external.feign.dto.OrderResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${feign.client.order.name}",
        url = "${feign.client.order.url}",
        path = "/orders")
public interface OrderClient {

    @GetMapping("/{id}")
    OrderResp getOrder(@PathVariable("id") Long orderId);

    @PostMapping("/verify")
    void verifyOrder(@RequestParam("orderId") Long orderId);
}
