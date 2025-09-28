package com.paxtech.utime.platform.reservations.interfaces.rest.resources;

public record CreatePaymentResource(float amount, String currency, boolean status) {

    public CreatePaymentResource {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than 0");
        if (currency == null || currency.isBlank()) throw new IllegalArgumentException("Currency must not be null or blank");
    }
}
