package com.example.product.dto.resp;

import lombok.Data;

import java.util.List;

@Data
public class OrderedProductResp {
    List<ProductResp> products;

    public OrderedProductResp(List<ProductResp> products) {
        this.products = products;
    }
}
