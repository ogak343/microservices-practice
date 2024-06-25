package com.example.order.service.impl;

import com.example.order.client.ProductFeignClient;
import com.example.order.dto.req.OrderCreate;
import com.example.order.dto.req.OrderUpdate;
import com.example.order.dto.resp.OrderResp;
import com.example.order.entity.ProductDetails;
import com.example.order.mapper.OrderMapper;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final ProductFeignClient productClient;

    @Override
    public OrderResp create(OrderCreate dto) {
        return null;
    }

    @Override
    public OrderResp update(OrderUpdate dto) {
        return null;
    }

    @Override
    public OrderResp get(Long id) {

        var order = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        var products = productClient.getProductDetails(order.getProductDetails()
                .stream().map(ProductDetails::getId).collect(Collectors.toSet()));

        var resp = mapper.toResp(order);

        resp.setProductDetails(products);

        return resp;
    }

    @Override
    public Page<OrderResp> getPage(Integer page, Integer size) {
        return null;
    }
}
