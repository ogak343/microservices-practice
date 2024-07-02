package com.example.order.external.messageBroker.dto;

import com.example.order.dto.resp.ProductResp;
import com.example.order.external.dto.OrderedProductResp;
import lombok.Data;

import java.util.Set;

@Data
public class OrderCreatePublishDto {

    private Long customerId;
    private Set<ProductResp> orderedProducts;

    public OrderCreatePublishDto(Long customerId, OrderedProductResp orderedProducts) {
        this.customerId = customerId;
        this.orderedProducts = orderedProducts.getProducts();
    }
}
