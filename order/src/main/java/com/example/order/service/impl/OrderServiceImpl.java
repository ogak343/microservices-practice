package com.example.order.service.impl;

import com.example.order.config.exception.CustomException;
import com.example.order.contants.ErrorCode;
import com.example.order.dto.resp.InfoResp;
import com.example.order.entity.Order;
import com.example.order.contants.Status;
import com.example.order.dto.req.OrderCreate;
import com.example.order.dto.req.OrderUpdate;
import com.example.order.dto.resp.OrderResp;
import com.example.order.entity.ProductDetails;
import com.example.order.external.ProductService;
import com.example.order.external.messageBroker.KafkaProducer;
import com.example.order.external.messageBroker.dto.SaveOrderDto;
import com.example.order.mapper.OrderMapper;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final KafkaProducer kafkaProducer;
    private final ProductService productClient;

    public OrderServiceImpl(OrderRepository repository,
                            OrderMapper mapper,
                            KafkaProducer kafkaProducer,
                            ProductService productClient) {
        this.repository = repository;
        this.mapper = mapper;
        this.kafkaProducer = kafkaProducer;
        this.productClient = productClient;
    }

    @Override
    public InfoResp create(OrderCreate dto) {

        var customerId = getCustomerId();

        var orderedProducts = productClient.createOrder(dto);

        kafkaProducer.publishOrderCreation(new SaveOrderDto(customerId, orderedProducts));

        return new InfoResp(200, Status.IN_CREATION_PROCESS.name());
    }

    private String getCustomerId() {
        var jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return jwt.getSubject();
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

    @Override
    public void verify(Long orderId) {

        var order = repository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        order.setStatus(Status.PAID);
        repository.save(order);
    }

    public void save(SaveOrderDto dto) {
        Order order;
        if (dto.getOrderId() != null) {
            order = repository.findById(dto.getOrderId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
            var map = order.getProductDetails().stream().collect(Collectors.toMap(ProductDetails::getProductId, productDetails -> productDetails));
            dto.getProducts().forEach(product -> {
                var entity = map.get(product.id());
                entity.setQuantity(entity.getQuantity() + product.quantity());
            });

        } else {
            order = mapper.toEntity(dto);
        }
        order.setStatus(Status.WAITING_FOR_PAYMENT);
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
