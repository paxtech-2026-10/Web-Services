package com.paxtech.utime.platform.reservations.domain.model.commands;

import java.math.BigDecimal;

public record CreatePaymentLinkCommand(
        Long reservationId,
        Long clientId,
        BigDecimal amount,
        String currency,
        String description
) {
    public CreatePaymentLinkCommand {
        if (reservationId == null || reservationId <= 0) {
            throw new IllegalArgumentException("ReservationId cannot be null or less than 1");
        }
        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("ClientId cannot be null or less than 1");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
    }
}

