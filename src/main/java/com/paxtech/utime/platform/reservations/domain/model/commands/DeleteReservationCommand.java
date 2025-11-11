package com.paxtech.utime.platform.reservations.domain.model.commands;

public record DeleteReservationCommand(Long id) {
    public DeleteReservationCommand {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("Reservation ID must be greater than 0");
        }
    }
}

