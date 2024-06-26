package com.example.product.dto.resp;

import lombok.Data;

import java.math.BigInteger;
import java.util.List;

@Data
public class OrderedProductResp {
    private BigInteger totalPrice = BigInteger.ZERO;
    List<ProductResp> products;
}
