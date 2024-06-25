package com.example.order.dto.req;

import lombok.Data;

@Data
public class ProductDetailsReq {
    private Long productId;
    private Integer quantity;
}
