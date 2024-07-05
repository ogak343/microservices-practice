package com.example.order.dto.req;

import com.example.order.annoration.Unrepeated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class OrderUpdate {
    @NotNull
    private Long orderId;
    @Unrepeated
    @NotEmpty
    private List<ProductDetailsDto> productDetails;
}
