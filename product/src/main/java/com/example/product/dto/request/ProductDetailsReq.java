package com.example.product.dto.request;

import lombok.Data;

@Data
public class ProductDetailsReq {
    private Long productId;
    private Integer quantity;
}
