package com.paxtech.utime.platform.reservations.interfaces.rest.transform;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.Reservation;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.ReservationResource;

public class ReservationResourceFromEntityAssembler {

    public static ReservationResource toResourceFromEntity(Reservation reservation) {
        return new ReservationResource(
                reservation.getId(),
                reservation.getClientId(),
                reservation.getProviderId(),
                reservation.getPaymentId(),
                reservation.getTimeSlotId(),
                reservation.getWorkerId()
        );
    }
}
