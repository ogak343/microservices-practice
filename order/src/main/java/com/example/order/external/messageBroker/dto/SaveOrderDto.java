package com.example.order.external.messageBroker.dto;

import com.example.order.dto.resp.ProductResp;
import com.example.order.external.dto.OrderedProductResp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveOrderDto {

    private Long orderId;
    private Long customerId;
    private Set<ProductResp> products;

    public SaveOrderDto(Long customerId, OrderedProductResp products) {
        this.customerId = customerId;
        this.products = products.getProducts();
    }
}
