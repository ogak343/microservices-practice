package com.example.order.client;

import com.example.order.dto.resp.ProductResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@FeignClient(name = "${feign.clients.product.name}",
        url = "${feign.clients.product.url}",
        path = "/products")
public interface ProductFeignClient {

    @GetMapping("/page")
    Set<ProductResp> getProductDetails(@RequestParam Set<Long> ids);




}
