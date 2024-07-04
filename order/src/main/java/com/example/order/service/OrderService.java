package com.example.order.service;

import com.example.order.dto.req.OrderCreate;
import com.example.order.dto.req.OrderUpdate;
import com.example.order.dto.resp.InfoResp;
import com.example.order.dto.resp.OrderResp;
import com.example.order.external.messageBroker.dto.OrderCreatePublishDto;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {
    @Transactional
    InfoResp create(OrderCreate dto);

    OrderResp update(OrderUpdate dto);

    OrderResp get(Long id);

    Page<OrderResp> getPage(Integer page, Integer size);

    void save(OrderCreatePublishDto dto);
}
