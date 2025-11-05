package com.paxtech.utime.platform.reservations.domain.model.commands;

public record CreateReservationCommand(
        Long clientId,
        Long providerId,
        Long serviceId,
        Long timeSlotId,
        Long workerId
) {}
