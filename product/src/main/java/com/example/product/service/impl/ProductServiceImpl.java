package com.example.product.service.impl;

import com.example.product.config.exception.CustomException;
import com.example.product.contants.ErrorCode;
import com.example.product.dto.SaveOrderDto;
import com.example.product.dto.request.*;
import com.example.product.dto.resp.OrderedProductResp;
import com.example.product.dto.resp.ProductResp;
import com.example.product.entity.Product;
import com.example.product.mapper.ProductMapper;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.ProductRepository;
import com.example.product.service.ProductService;
import com.example.product.service.eventHandler.KafkaProducer;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service(value = "PRODUCT")
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;
    private final KafkaProducer kafkaProducer;

    @Override
    public ProductResp create(ProductCreateReq product) {

        var entity = mapper.toEntity(product);

        entity.setCategory(categoryRepository.findById(product.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.CATEGORY_NOT_FOUND)));

        return mapper.toResp(repository.save(entity));
    }

    @Override
    public ProductResp get(Long id) {
        return mapper.toResp(repository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND)));
    }

    @Override
    public ProductResp update(ProductUpdateReq product) {

        var entity = repository.findById(product.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        mapper.update(entity, product);
        return mapper.toResp(repository.save(entity));
    }

    @Override
    public void delete(Long id) {

        repository.deleteById(id);
    }

    @Override
    public Page<ProductResp> search(int page, int size, Long categoryId, String nameLike) {
        return repository.findAll(makeSpecification(categoryId, nameLike), PageRequest.of(page, size))
                .map(mapper::toResp);
    }

    @Override
    public OrderedProductResp createOrder(OrderCreate order) {

        var products = repository.findAllById(order.getProducts().keySet());

        if (!Objects.equals(products.size(), order.getProducts().size()))
            throw new CustomException(ErrorCode.NOT_ALL_PRODUCTS_FOUND);

        products = products.stream()
                .peek(product -> {
                    if (product.getQuantity() < order.getProducts().get(product.getId())) {
                        throw new CustomException(ErrorCode.INSUFFICIENT_QUANTITY);
                    }
                    product.setQuantity(product.getQuantity() - order.getProducts().get(product.getId()));
                }).toList();

        repository.saveAll(products);
        return new OrderedProductResp(products.stream().map(product ->
                mapper.toOrderedProduct(product, order.getProducts().get(product.getId()))).toList());
    }

    private Specification<Product> makeSpecification(Long categoryId, String nameLike) {
        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();
            if (categoryId != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), categoryId));
            }
            if (nameLike != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + nameLike + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public void modify(OrderUpdate dto) {

        var map = dto.getProductDetails().stream()
                .collect(Collectors.toMap(ProductDetailsReq::getProductId, ProductDetailsReq::getQuantity));

        var products = repository.findAllById(map.keySet());

        if (products.size() != map.size()) {
            throw new CustomException(ErrorCode.NOT_ALL_PRODUCTS_FOUND);
        }

        products.forEach(product -> {
            if (product.getQuantity() < map.get(product.getId())) {
                throw new CustomException(ErrorCode.INSUFFICIENT_QUANTITY);
            } else {
                product.setQuantity(product.getQuantity() - map.get(product.getId()));
            }
        });
        products = repository.saveAll(products);

        kafkaProducer.sendMessage(new SaveOrderDto(dto.getOrderId(), products.stream()
                .map(product -> mapper.toOrderedProduct(product, map.get(product.getId()))).collect(Collectors.toSet())));

    }
}
