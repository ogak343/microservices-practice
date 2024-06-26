package com.example.order.service.impl;

import com.example.order.client.CustomerFeignClient;
import com.example.order.client.ProductFeignClient;
import com.example.order.contants.Status;
import com.example.order.dto.req.OrderCreate;
import com.example.order.dto.req.OrderUpdate;
import com.example.order.dto.resp.OrderResp;
import com.example.order.entity.Order;
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
    private final CustomerFeignClient customerClient;

    @Override
    public OrderResp create(OrderCreate dto) {

        var customer = customerClient.validateCustomer();

        var price = productClient.orderProducts(dto);

        var order = new Order(price, customer.getId());

        var details = dto.getProducts().entrySet().stream()
                .map(entry -> new ProductDetails(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());
        order.setProductDetails(details);

        order.setStatus(Status.WAITING_FOR_PAYMENT);
        order.getProductDetails().forEach(x -> x.setOrder(order));
        return mapper.toResp(repository.save(order));
    }

    @Override
    public OrderResp update(OrderUpdate dto) {

        //TODO
        return null;
    }

    @Override
    public OrderResp get(Long id) {

        var order = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        var products = productClient.productDetails(order.getProductDetails()
                .stream().map(ProductDetails::getId).collect(Collectors.toSet()));

        var resp = mapper.toResp(order);
        resp.setProductDetails(products);

        return resp;
    }

    @Override
    public Page<OrderResp> getPage(Integer page, Integer size) {
        //TODO
        return null;
    }
}
