package com.example.payment.external.feign;

import com.example.payment.config.FeignClientInterceptor;
import com.example.payment.external.feign.dto.CustomerResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "${feign.client.customer.name}",
            url = "${feign.client.customer.url}",
            path = "/customers",
            configuration = FeignClientInterceptor.class)
public interface CustomerClient {

    @PostMapping("/profile")
    CustomerResp profile();
}
