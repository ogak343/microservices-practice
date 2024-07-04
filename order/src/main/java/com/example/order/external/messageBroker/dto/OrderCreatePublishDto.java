package com.example.order.external.messageBroker.dto;

import com.example.order.dto.resp.ProductResp;
import com.example.order.external.dto.OrderedProductResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreatePublishDto {

    private Long customerId;
    private Set<ProductResp> orderedProducts;

    public OrderCreatePublishDto(Long customerId, OrderedProductResp orderedProducts) {
        this.customerId = customerId;
        this.orderedProducts = orderedProducts.getProducts();
    }
}
