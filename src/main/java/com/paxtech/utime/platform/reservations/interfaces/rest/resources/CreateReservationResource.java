package com.paxtech.utime.platform.reservations.interfaces.rest.resources;

public record CreateReservationResource(
        Long clientId,
        Long providerId,
        Long serviceId,
        Long timeSlotId,
        Long workerId
) {}
