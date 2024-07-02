package com.example.order.external.dto;

import com.example.order.dto.resp.ProductResp;

import lombok.Data;

import java.util.Set;

@Data
public class OrderedProductResp {
    private Set<ProductResp> products;
}
