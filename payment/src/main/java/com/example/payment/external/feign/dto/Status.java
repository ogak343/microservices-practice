package com.example.payment.external.feign.dto;

public enum Status {
    IN_CREATION_PROCESS,
    WAITING_FOR_PAYMENT,
    PAID,
    IN_UPDATE_PROCESS,
    CANCELLED
}
