package com.example.order.client;

import com.example.order.dto.req.OrderCreate;
import com.example.order.dto.req.ProductDetailsReq;
import com.example.order.dto.resp.ProductResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.Map;
import java.util.Set;

@FeignClient(name = "${feign.clients.product.name}",
        url = "${feign.clients.product.url}",
        path = "/products")
public interface ProductFeignClient {

    @GetMapping("/page")
    Set<ProductResp> productDetails(@RequestParam Set<Long> ids);

    @PostMapping("/order")
    BigInteger orderProducts(@RequestBody Map<Long, Integer> orderCreate);
}
