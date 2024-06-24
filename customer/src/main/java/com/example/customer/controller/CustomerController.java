package com.example.customer.controller;

import com.example.customer.dto.req.CustomerConfirmReq;
import com.example.customer.dto.req.CustomerCreateReq;
import com.example.customer.dto.req.CustomerUpdateReq;
import com.example.customer.dto.req.LoginReq;
import com.example.customer.dto.resp.CustomerResp;
import com.example.customer.dto.resp.LoginResp;
import com.example.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class CustomerController {

    private final CustomerService service;

    @PostMapping("/create")
    public ResponseEntity<Long> create(@RequestBody @Valid CustomerCreateReq customer) {

        log.info("CustomerCreateReq: {}", customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(customer));
    }

    @PostMapping("/confirm")
    public ResponseEntity<LoginResp> confirm(@RequestBody @Valid CustomerConfirmReq customer) {

        log.info("CustomerConfirmReq: {}", customer);

        return ResponseEntity.ok(service.confirm(customer));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResp> login(@RequestBody @Valid LoginReq login) {

        log.info("CustomerLoginReq: {}", login);

        return ResponseEntity.ok(service.login(login));
    }

    @GetMapping("/profile")
    public ResponseEntity<CustomerResp> profile() {

        log.info("CustomerGetProfile: ");

        return ResponseEntity.ok(service.profile());
    }

    @PatchMapping
    public ResponseEntity<CustomerResp> update(@RequestBody @Valid CustomerUpdateReq dto) {

        log.info("CustomerUpdateReq: {}", dto);

        return ResponseEntity.ok(service.update(dto));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete() {

        log.info("CustomerDelete called!");
        service.delete();
        return ResponseEntity.ok().build();
    }

}
