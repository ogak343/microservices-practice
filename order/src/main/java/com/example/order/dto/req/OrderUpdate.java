package com.example.order.dto.req;

import lombok.Data;

import java.util.Set;

@Data
public class OrderUpdate {
    private Long orderId;
    //TODO implement constraint for duplication of Ids with Custom validation @ annotation
    private Set<ProductDetailsReq> productDetails;
}
