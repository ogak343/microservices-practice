package com.example.product.controller;

import com.example.product.dto.request.OrderCreate;
import com.example.product.dto.resp.ProductResp;
import com.example.product.dto.request.ProductCreateReq;
import com.example.product.dto.request.ProductUpdateReq;
import com.example.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService service;


    @PostMapping
    public ResponseEntity<ProductResp> create(@RequestBody @Valid ProductCreateReq product) {

        log.info("Create product: {}", product);

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(product));
    }

    @PostMapping("/order")
    public ResponseEntity<BigInteger> createOrder(@RequestBody @Valid OrderCreate order) {

        log.info("Create order: {}", order);
        return ResponseEntity.ok(service.order(order));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ProductResp>> search(@RequestParam int page,
                                                    @RequestParam int size,
                                                    @RequestParam(required = false) Long categoryId,
                                                    @RequestParam(required = false) String nameLike) {

        log.info("Get product page: {}, size: {}, categoryId: {}, nameLike: {}", page, size, categoryId, nameLike);

        return ResponseEntity.ok(service.search(page, size, categoryId, nameLike));
    }

    @GetMapping("/byIds")
    public ResponseEntity<List<ProductResp>> getAll(@RequestParam List<Long> ids) {

        log.info("Get product by ids: {}", ids);
        return ResponseEntity.ok(service.getAllByIds(ids));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResp> get(@PathVariable Long id) {

        log.info("Get product: {}", id);

        return ResponseEntity.ok(service.get(id));
    }

    @PatchMapping
    public ResponseEntity<ProductResp> update(@RequestBody @Valid ProductUpdateReq product) {

        log.info("Update product: {}", product);

        return ResponseEntity.ok(service.update(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        log.info("Delete product: {}", id);

        service.delete(id);
        return ResponseEntity.ok().build();
    }


}
