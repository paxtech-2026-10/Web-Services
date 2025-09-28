package com.paxtech.utime.platform.reservations.domain.services;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.Reservation;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetAllReservationsQuery;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetReservationByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ReservationQueryService {
    List<Reservation> handle(GetAllReservationsQuery query);
    Optional<Reservation> handle(GetReservationByIdQuery query);
}
