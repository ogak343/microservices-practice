package com.example.order.external.client;

import com.example.order.config.helpers.FeignClientDecoder;
import com.example.order.external.dto.CustomerResp;
import com.example.order.config.helpers.FeignClientInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "${feign.clients.customer.name}",
        url = "${feign.clients.customer.url}",
        path = "/customers",
        configuration = {FeignClientInterceptor.class, FeignClientDecoder.class})
public interface CustomerFeignClient {

    @GetMapping("/profile")
    CustomerResp validateCustomer();
}
