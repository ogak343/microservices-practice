package com.example.order.dto.req;

import lombok.Data;

import java.util.Objects;

@Data
public class ProductDetailsReq {
    private Long productId;
    private Integer quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductDetailsReq that = (ProductDetailsReq) o;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productId);
    }
}
