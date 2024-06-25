package com.example.order.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResp {
    private Long id;
    private String name;
    private String description;
    private BigInteger price;
    private int quantity;
}
