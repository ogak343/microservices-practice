package com.example.order.client;

import com.example.order.config.FeignClientConfig;
import com.example.order.dto.req.OrderCreate;
import com.example.order.dto.resp.ProductResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.Set;

@FeignClient(name = "${feign.clients.product.name}",
        url = "${feign.clients.product.url}",
        path = "/products",
        configuration = FeignClientConfig.class)
public interface ProductFeignClient {

    @GetMapping("/page")
    Set<ProductResp> productDetails(@RequestParam Set<Long> ids);

    @PostMapping("/order")
    ResponseEntity<BigInteger> orderProducts(@RequestBody OrderCreate orderCreate);
}
