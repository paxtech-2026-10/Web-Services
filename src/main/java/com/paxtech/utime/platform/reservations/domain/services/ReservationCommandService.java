package com.paxtech.utime.platform.reservations.domain.services;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.Reservation;
import com.paxtech.utime.platform.reservations.domain.model.commands.CreateReservationCommand;

import java.util.Optional;

public interface ReservationCommandService {
    Optional<Reservation> handle(CreateReservationCommand command);
}
