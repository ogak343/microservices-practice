package com.example.payment.controller;

import com.example.payment.dto.InfoResp;
import com.example.payment.dto.PaymentPerformDto;
import com.example.payment.dto.PaymentResponse;
import com.example.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService service;

    @PostMapping("/create")
    public ResponseEntity<InfoResp<Long>> createPayment(@RequestParam("orderId") Long orderId) {

        log.info("PaymentCreate orderID : {}", orderId);
        return ResponseEntity.ok(new InfoResp<>(service.create(orderId)));
    }

    @PostMapping("/perform")
    public ResponseEntity<InfoResp<String>> performPayment(@RequestBody PaymentPerformDto payment) {

        log.info("PaymentPerformDto : {}", payment);

        return ResponseEntity.ok(new InfoResp<>(service.perform(payment)));
    }
}
