package com.example.order.controller;

import com.example.order.dto.req.OrderCreate;
import com.example.order.dto.req.OrderUpdate;
import com.example.order.dto.resp.OrderResp;
import com.example.order.service.OrderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<OrderResp> create(@RequestBody @Valid OrderCreate dto) {

        log.info("Create order: {}", dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));
    }

    @PutMapping
    public ResponseEntity<OrderResp> update(@RequestBody @Valid OrderUpdate dto) {

        log.info("Update order: {}", dto);
        return ResponseEntity.ok(service.update(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResp> get(@PathVariable Long id) {

        log.info("Get order: {}", id);
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/myOrders")
    public ResponseEntity<Page<OrderResp>> getMyOrders(@RequestParam(name = "page") Integer page,
                                                       @RequestParam(name = "size") Integer size) {

        log.info("Get orders! page: {}, size {}", page, size);
        return ResponseEntity.ok(service.getPage(page, size));
    }
}
