package com.paxtech.utime.platform.reservations.interfaces.rest.resources;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.TimeSlot;
import com.paxtech.utime.platform.reservations.interfaces.rest.acl.ProviderDto;
import com.paxtech.utime.platform.reservations.interfaces.rest.acl.WorkerDto;

public record ReservationDetailsResource(
        Long id, Long clientId, ProviderDto provider, PaymentResource paymentId, TimeSlotResource timeSlot, WorkerDto workerId) {
}
