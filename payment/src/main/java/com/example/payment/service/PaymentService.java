package com.example.payment.service;

import com.example.payment.dto.PaymentPerformDto;

public interface PaymentService {
    Long create(Long orderId);

    String perform(PaymentPerformDto payment);
}
