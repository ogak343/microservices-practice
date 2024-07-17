package com.example.product.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderUpdate(
        @NotNull Long orderId,
        @NotEmpty List<ProductDetailsReq> productDetails
) {

}
