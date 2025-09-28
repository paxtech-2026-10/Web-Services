package com.paxtech.utime.platform.reservations.interfaces.rest.resources;

public record CreateReservationResource(
        Long clientId,
        Long providerId,
        Long paymentId,
        Long timeSlotId,
        Long workerId
) {}
