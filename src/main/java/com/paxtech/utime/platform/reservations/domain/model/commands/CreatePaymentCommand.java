package com.paxtech.utime.platform.reservations.domain.model.commands;

public record CreatePaymentCommand(
        float amount,
        String currency,
        Long reservationId,
        Long clientId
) {
    public CreatePaymentCommand {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or empty");
        }
        if (reservationId == null || reservationId <= 0) {
            throw new IllegalArgumentException("ReservationId cannot be null or less than 1");
        }
        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("ClientId cannot be null or less than 1");
        }
    }
}
