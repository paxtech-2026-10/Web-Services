package com.paxtech.utime.platform.reservations.interfaces.rest.resources;

public record CreatePaymentResource(
        float amount,
        String currency,
        Long reservationId,
        Long clientId
) {
    public CreatePaymentResource {
        if (amount <= 0) throw new IllegalArgumentException("Amount must be greater than 0");
        if (currency == null || currency.isBlank()) throw new IllegalArgumentException("Currency must not be null or blank");
        if (reservationId == null || reservationId <= 0) throw new IllegalArgumentException("ReservationId must be greater than 0");
        if (clientId == null || clientId <= 0) throw new IllegalArgumentException("ClientId must be greater than 0");
    }
}
