package com.example.product.service.impl;

import com.example.product.config.exception.CustomException;
import com.example.product.contants.ErrorCode;
import com.example.product.dto.request.OrderCreate;
import com.example.product.dto.request.ProductCreateReq;
import com.example.product.dto.request.ProductUpdateReq;
import com.example.product.dto.resp.ProductResp;
import com.example.product.entity.Product;
import com.example.product.mapper.ProductMapper;
import com.example.product.repository.CategoryRepository;
import com.example.product.repository.ProductRepository;
import com.example.product.service.ProductService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service(value = "PRODUCT")
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper mapper;

    @Override
    public ProductResp create(ProductCreateReq product) {

        var entity = mapper.toEntity(product);

        entity.setCategory(categoryRepository.findById(product.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found")));

        return mapper.toResp(repository.save(entity));
    }

    @Override
    public ProductResp get(Long id) {
        return mapper.toResp(repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found")));
    }

    @Override
    public ProductResp update(ProductUpdateReq product) {

        var entity = repository.findById(product.getId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

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
    public List<ProductResp> getAllByIds(List<Long> ids) {
        return repository.findAllById(ids).stream().map(mapper::toResp).toList();
    }

    @Override
    public BigInteger order(OrderCreate order) {

        var products = repository.findAllByIdIn(order.getProducts().keySet());

        var price = products.stream()
                .peek(x -> {
                    if (x.getQuantity() < order.getProducts().get(x.getId())) {
                        throw new CustomException(ErrorCode.INVALID_AMOUNT);
                    } else {
                        x.setQuantity(x.getQuantity() - order.getProducts().get(x.getId()));
                    }
                })
                .map(Product::getPrice)
                .reduce(BigInteger.ZERO, BigInteger::add);

        repository.saveAll(products);

        log.info("price : {}", price);
        return price;
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
}
