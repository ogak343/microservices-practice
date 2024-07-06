package com.example.product.dto;

import com.example.product.dto.resp.ProductResp;
import lombok.Data;

import java.util.Set;

@Data
public class SaveOrderDto {
    private Long orderId;
    private Long customerId;
    private Set<ProductResp> products;

    public SaveOrderDto(Long orderId, Set<ProductResp> products) {
        this.orderId = orderId;
        this.products = products;
    }
}
