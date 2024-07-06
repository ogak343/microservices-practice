package com.example.product.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderUpdate {
    @NotNull
    private Long orderId;
    @NotEmpty
    private List<ProductDetailsReq> productDetails;

}
