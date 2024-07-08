package com.example.order.external.client;

import com.example.order.config.helpers.FeignClientDecoder;
import com.example.order.external.dto.OrderedProductResp;
import com.example.order.config.helpers.FeignClientInterceptor;
import com.example.order.dto.req.OrderCreate;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "${feign.clients.product.name}",
        url = "${feign.clients.product.url}",
        path = "/products",
        configuration = {FeignClientInterceptor.class, FeignClientDecoder.class})
public interface ProductFeignClient {

    @PostMapping("/order/create")
    OrderedProductResp createOrder(@RequestBody OrderCreate orderCreate);

}
