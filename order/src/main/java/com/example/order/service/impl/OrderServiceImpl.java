package com.example.order.service.impl;

import com.example.order.config.exception.CustomException;
import com.example.order.contants.ErrorCode;
import com.example.order.dto.resp.InfoResp;
import com.example.order.entity.Order;
import com.example.order.external.client.CustomerFeignClient;
import com.example.order.external.client.ProductFeignClient;
import com.example.order.contants.Status;
import com.example.order.dto.req.OrderCreate;
import com.example.order.dto.req.OrderUpdate;
import com.example.order.dto.resp.OrderResp;
import com.example.order.entity.ProductDetails;
import com.example.order.external.messageBroker.KafkaProducer;
import com.example.order.external.messageBroker.dto.SaveOrderDto;
import com.example.order.mapper.OrderMapper;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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

        var orderedProducts = productClient.createOrder(dto);

        kafkaProducer.publishOrderCreation(new SaveOrderDto(customer.getId(), orderedProducts));

        return new InfoResp(200, Status.IN_CREATION_PROCESS.name());
    }

    @Override
    public InfoResp update(OrderUpdate dto) {

        if (!repository.existsByIdAndStatus(dto.getOrderId(), Status.WAITING_FOR_PAYMENT))
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);

        kafkaProducer.publishOrderModification(dto);

        return new InfoResp(200, String.format(Status.IN_UPDATE_PROCESS.name()));
    }

    @Override
    public OrderResp get(Long id) {
        return mapper.toResp(repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND)));
    }


    @Override
    public Page<OrderResp> getPage(Integer page, Integer size) {

        var pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("id")));

        return repository.findAll(pageable).map(mapper::toResp);

    }

    public void save(SaveOrderDto dto) {
        Order order;
        if (dto.getOrderId() != null) {
            order = repository.findById(dto.getOrderId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
        } else {
            order = mapper.toEntity(dto);
        }
        order.setTotalPrice(calculatePrice(order.getProductDetails()));
        order.getProductDetails().forEach(x -> x.setOrder(order));
        repository.save(order);
    }

    public void modify(OrderUpdate dto) {

        var order = repository.findByIdAndStatus(dto.getOrderId(), Status.WAITING_FOR_PAYMENT)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        var map = order.getProductDetails().stream()
                .collect(Collectors.toMap(ProductDetails::getProductId, ProductDetails::getQuantity));

        dto.getProductDetails().forEach(product -> {
            if (map.get(product.getProductId()) != null) {
                product.setQuantity(product.getQuantity() - map.get(product.getProductId()));
            }
        });

        order.setStatus(Status.IN_UPDATE_PROCESS);
        repository.save(order);

        kafkaProducer.publishOrderModificationToProduct(dto);
    }

    private BigInteger calculatePrice(Set<ProductDetails> productDetails) {

        BigInteger totalPrice = BigInteger.ZERO;
        for (ProductDetails product : productDetails) {
            totalPrice = totalPrice.add(product.getPrice().multiply(BigInteger.valueOf(product.getQuantity())));
        }
        return totalPrice;
    }
}
