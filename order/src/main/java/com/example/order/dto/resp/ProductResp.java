package com.example.order.dto.resp;

import lombok.Data;

import java.math.BigInteger;

@Data
public class ProductResp {
    private Long id;
    private String name;
    private String description;
    private BigInteger price;
    private int quantity;
}
