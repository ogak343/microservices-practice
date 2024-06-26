package com.example.order.dto.resp;

import com.example.order.contants.Status;
import lombok.Data;
import org.mapstruct.Mapping;

import java.math.BigInteger;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
public class OrderResp {
    private Long id;
    private Set<ProductResp> productDetails;
    private BigInteger totalPrice;
    private Long customerId;
    private Status status;
    private OffsetDateTime createdAt;
    private OffsetDateTime payedAt;
}
