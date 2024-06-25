package com.example.order.client;

import com.example.order.client.dto.CustomerResp;
import com.example.order.config.FeignClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${feign.clients.customer.name}",
        url = "${feign.clients.customer.url}",
        path = "/customers",
        configuration = FeignClientConfig.class)
public interface CustomerFeignClient {

    @GetMapping("/profile")
    CustomerResp validateCustomer();
}
