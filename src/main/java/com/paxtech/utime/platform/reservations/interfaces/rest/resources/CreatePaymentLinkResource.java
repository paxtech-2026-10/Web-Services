package com.paxtech.utime.platform.reservations.interfaces.rest.resources;

import java.math.BigDecimal;

public record CreatePaymentLinkResource(
        Long paymentId,
        BigDecimal amount,
        String currency,
        String description
) {
    public CreatePaymentLinkResource {
        if (paymentId == null || paymentId <= 0) {
            throw new IllegalArgumentException("PaymentId must be greater than 0");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency must not be null or blank");
        }
    }
}

