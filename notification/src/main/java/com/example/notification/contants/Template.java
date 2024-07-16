package com.example.notification.contants;

import lombok.Getter;

@Getter
public enum Template {
    PAYMENT_CREATE("PaymentCreation"),
    PAYMENT_PERFORM("PaymentPerform"),
    ACCOUNT_VERIFICATION("AccountVerification"),;

    private final String value;

    Template(String value) {
        this.value = value;
    }
}
