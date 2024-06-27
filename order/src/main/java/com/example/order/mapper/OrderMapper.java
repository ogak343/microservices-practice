package com.example.order.mapper;

import com.example.order.dto.resp.OrderResp;
import com.example.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(target = "productDetails.quantity", ignore = true)
    OrderResp toResp(Order order);

}
