package com.paxtech.utime.platform.reservations.interfaces.rest.resources;

import com.paxtech.utime.platform.reservations.interfaces.rest.acl.ClientDto;
import com.paxtech.utime.platform.reservations.interfaces.rest.acl.ProviderDto;
import com.paxtech.utime.platform.reservations.interfaces.rest.acl.WorkerDto;
import com.paxtech.utime.platform.services.interfaces.rest.resources.ServiceResource;

public record ReservationDetailsResource(
        Long id, Long clientId, ClientDto client, ProviderDto provider, ServiceResource serviceId, TimeSlotResource timeSlot, WorkerDto workerId, PaymentSummaryDto paymentId) {
}
