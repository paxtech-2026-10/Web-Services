package com.paxtech.utime.platform.reservations.interfaces.rest.resources;

import com.paxtech.utime.platform.reservations.domain.model.valueobjects.PaymentStatus;

public record PaymentResource(
        Long id,
        float amount,
        String currency,
        PaymentStatus paymentStatus,
        String stripePaymentLinkId,
        String stripeCheckoutSessionId,
        Long reservationId,
        Long clientId
) {
}
