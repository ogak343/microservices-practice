package com.example.order.service.impl;

import com.example.order.dto.resp.InfoResp;
import com.example.order.external.client.CustomerFeignClient;
import com.example.order.external.client.ProductFeignClient;
import com.example.order.contants.Status;
import com.example.order.dto.req.OrderCreate;
import com.example.order.dto.req.OrderUpdate;
import com.example.order.dto.resp.OrderResp;
import com.example.order.entity.ProductDetails;
import com.example.order.external.messageBroker.KafkaProducer;
import com.example.order.external.messageBroker.dto.OrderCreatePublishDto;
import com.example.order.mapper.OrderMapper;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final ProductFeignClient productClient;
    private final CustomerFeignClient customerClient;
    private final KafkaProducer kafkaProducer;

    @Override
    public InfoResp create(OrderCreate dto) {

        var customer = customerClient.validateCustomer();

        var orderedProducts = productClient.orderProducts(dto);

        kafkaProducer.publishOrderCreation(new OrderCreatePublishDto(customer.getId(), orderedProducts));

        return new InfoResp(200, Status.IN_CREATION_PROCESS.name());
    }

    @Override
    public OrderResp update(OrderUpdate dto) {

        //TODO
        return null;
    }

    @Override
    public OrderResp get(Long id) {
        return mapper.toResp(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found")));
    }

    @Override
    public Page<OrderResp> getPage(Integer page, Integer size) {

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")));

        return repository.findAll(pageable).map(mapper::toResp);

    }

    @Override
    public void save(OrderCreatePublishDto dto) {

        var order = mapper.toEntity(dto);
        order.setStatus(Status.WAITING_FOR_PAYMENT);
        order.setTotalPrice(calculatePrice(order.getProductDetails()));
        order.getProductDetails().forEach(x -> x.setOrder(order));
        repository.save(order);
    }

    private BigInteger calculatePrice(Set<ProductDetails> productDetails) {

        BigInteger totalPrice = BigInteger.ZERO;
        for (ProductDetails product : productDetails) {
            totalPrice = totalPrice.add(product.getPrice().multiply(BigInteger.valueOf(product.getQuantity())));
        }
        return totalPrice;
    }
}
