package com.example.order.mapper;

import com.example.order.dto.req.ProductDetailsReq;
import com.example.order.dto.resp.OrderResp;
import com.example.order.entity.Order;
import com.example.order.entity.ProductDetails;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    OrderResp toResp(Order order);

    ProductDetails toEntity(ProductDetailsReq productResp);

}
