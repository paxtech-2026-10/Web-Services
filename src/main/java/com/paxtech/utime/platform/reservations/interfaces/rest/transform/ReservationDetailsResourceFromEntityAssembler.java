package com.paxtech.utime.platform.reservations.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.interfaces.acl.ProviderContextFacade;
import com.paxtech.utime.platform.reservations.domain.model.aggregates.Reservation;

import com.paxtech.utime.platform.reservations.domain.model.queries.GetTimeSlotByIdQuery;

import com.paxtech.utime.platform.reservations.domain.services.TimeSlotQueryService;
import com.paxtech.utime.platform.reservations.interfaces.rest.acl.ProviderDto;
import com.paxtech.utime.platform.reservations.interfaces.rest.acl.WorkerDto;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.ReservationDetailsResource;
import com.paxtech.utime.platform.services.domain.model.queries.GetServiceByIdQuery;
import com.paxtech.utime.platform.services.domain.services.ServiceQueryService;
import com.paxtech.utime.platform.services.interfaces.rest.transform.ServiceResourceFromEntityAssembler;
import com.paxtech.utime.platform.workers.interfaces.rest.acl.WorkerContextFacade;

public class ReservationDetailsResourceFromEntityAssembler {

    public static ReservationDetailsResource toResourceFromEntity(Reservation reservation, ProviderContextFacade providerContextFacade, TimeSlotQueryService timeSlotQueryService, WorkerContextFacade workerContextFacade, ServiceQueryService serviceQueryService) {

        var provider = providerContextFacade.fetchProviderById(reservation.getProviderId())
                .orElseThrow(() -> new IllegalArgumentException("Provider not found"));
        var providerDto = new ProviderDto(
                provider.getId(),
                provider.getUser().getEmail(),
                provider.getCompanyName()
        );

        // Manejar el caso cuando el Worker ha sido eliminado
        WorkerDto workerDto;
        var workerOpt = workerContextFacade.fetchWorkerById(reservation.getWorkerId());
        if (workerOpt.isPresent()) {
            var worker = workerOpt.get();
            workerDto = new WorkerDto(
                    worker.getId(),
                    worker.getName(),
                    worker.getSpecialization()
            );
        } else {
            // Worker eliminado - crear un DTO placeholder
            workerDto = WorkerDto.deleted(reservation.getWorkerId());
        }

        var timeSlotQuery = new GetTimeSlotByIdQuery(reservation.getTimeSlotId());
        var timeSlotResult = timeSlotQueryService.handle(timeSlotQuery);
        if (timeSlotResult.isEmpty()) {
            throw new IllegalArgumentException("TimeSlot not found");
        }
        var timeSLotResource = TimeSlotResourceFromEntityAssembler.toResourceFromEntity(timeSlotResult.get());

        // Manejar el caso cuando el Service ha sido eliminado
        var serviceQuery = new GetServiceByIdQuery(reservation.getServiceId());
        var serviceResult = serviceQueryService.handle(serviceQuery);
        var serviceResource = serviceResult
                .map(ServiceResourceFromEntityAssembler::toResourceFromEntity)
                .orElse(com.paxtech.utime.platform.services.interfaces.rest.resources.ServiceResource.deleted(
                        reservation.getServiceId(),
                        reservation.getProviderId()
                ));

        return new ReservationDetailsResource(
                reservation.getId(),
                reservation.getClientId(),
                providerDto,
                serviceResource,
                timeSLotResource,
                workerDto
        );
    }
}
