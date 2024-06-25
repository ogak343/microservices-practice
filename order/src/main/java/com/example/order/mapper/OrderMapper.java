package com.example.order.mapper;

import com.example.order.dto.resp.OrderResp;
import com.example.order.entity.Order;
import org.mapstruct.Mapper;

@Mapper
public interface OrderMapper {
    OrderResp toResp(Order order);
}
