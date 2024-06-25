package com.example.order.service;

import com.example.order.dto.req.OrderCreate;
import com.example.order.dto.req.OrderUpdate;
import com.example.order.dto.resp.OrderResp;
import org.springframework.data.domain.Page;

public interface OrderService {
    OrderResp create(OrderCreate dto);

    OrderResp update(OrderUpdate dto);

    OrderResp get(Long id);

    Page<OrderResp> getPage(Integer page, Integer size);
}
