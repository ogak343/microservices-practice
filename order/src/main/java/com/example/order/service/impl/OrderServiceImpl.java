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

import java.util.Map;
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

        var orderedProducts = productClient.orderProducts(dto);

        var order = new Order(orderedProducts.getTotalPrice(), customer.getId());

        var details = dto.getProducts().entrySet().stream()
                .map(entry -> new ProductDetails(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());
        order.setProductDetails(details);

        order.setStatus(Status.WAITING_FOR_PAYMENT);
        order.setTotalPrice(orderedProducts.getTotalPrice());
        order.getProductDetails().forEach(x -> x.setOrder(order));

        var response = mapper.toResp(repository.save(order));
        response.setProductDetails(orderedProducts.getProducts());

        return response;
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
                .stream().map(ProductDetails::getProductId).collect(Collectors.toSet()));

        var resp = mapper.toResp(order);

        Map<Long, Integer> map = order.getProductDetails()
                .stream().collect(Collectors.toMap(ProductDetails::getProductId, ProductDetails::getQuantity));

        products.forEach(productResp -> productResp.setQuantity(map.get(productResp.getId())));

        resp.setProductDetails(products);

        return resp;
    }

    @Override
    public Page<OrderResp> getPage(Integer page, Integer size) {
        //TODO
        return null;
    }
}
