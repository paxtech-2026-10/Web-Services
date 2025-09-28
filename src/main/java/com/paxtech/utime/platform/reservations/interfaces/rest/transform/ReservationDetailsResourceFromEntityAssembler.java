package com.paxtech.utime.platform.reservations.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.interfaces.acl.ProviderContextFacade;
import com.paxtech.utime.platform.reservations.domain.model.aggregates.Reservation;
import com.paxtech.utime.platform.reservations.domain.model.aggregates.TimeSlot;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetPaymentByIdQuery;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetTimeSlotByIdQuery;
import com.paxtech.utime.platform.reservations.domain.services.PaymentQueryService;
import com.paxtech.utime.platform.reservations.domain.services.TimeSlotQueryService;
import com.paxtech.utime.platform.reservations.interfaces.rest.acl.ProviderDto;
import com.paxtech.utime.platform.reservations.interfaces.rest.acl.WorkerDto;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.ReservationDetailsResource;
import com.paxtech.utime.platform.workers.interfaces.rest.acl.WorkerContextFacade;

public class ReservationDetailsResourceFromEntityAssembler {

    public static ReservationDetailsResource toResourceFromEntity(Reservation reservation, ProviderContextFacade providerContextFacade, TimeSlotQueryService timeSlotQueryService, WorkerContextFacade workerContextFacade, PaymentQueryService paymentQueryService) {

        var provider = providerContextFacade.fetchProviderById(reservation.getProviderId())
                .orElseThrow(() -> new IllegalArgumentException("Provider not found"));
        var providerDto = new ProviderDto(
                provider.getId(),
                provider.getUser().getEmail(),
                provider.getCompanyName()
        );

        var worker = workerContextFacade.fetchWorkerById(reservation.getWorkerId()).orElseThrow(() -> new IllegalArgumentException("Worker not found"));
        var workerDto = new WorkerDto(
                worker.getId(),
                worker.getName(),
                worker.getSpecialization()
        );

        var timeSlotQuery = new GetTimeSlotByIdQuery(reservation.getTimeSlotId());
        var timeSlotResult = timeSlotQueryService.handle(timeSlotQuery);
        if (timeSlotResult.isEmpty()) {
            throw new IllegalArgumentException("TimeSlot not found");
        }
        var timeSLotResource = TimeSlotResourceFromEntityAssembler.toResourceFromEntity(timeSlotResult.get());

        var paymentQuery = new GetPaymentByIdQuery(reservation.getPaymentId());
        var paymentResult = paymentQueryService.handle(paymentQuery);
        if (paymentResult.isEmpty()) throw new IllegalArgumentException("Payment not found");
        var paymentResource = PaymentResourceFromEntityAssembler.toResourceFromEntity(paymentResult.get());

        return new ReservationDetailsResource(
                reservation.getId(),
                reservation.getClientId(),
                providerDto,
                paymentResource,
                timeSLotResource,
                workerDto
        );
    }
}
