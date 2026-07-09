package com.paxtech.utime.platform.reservations.interfaces.rest;

import com.paxtech.utime.platform.profiles.interfaces.acl.ClientContextFacade;
import com.paxtech.utime.platform.profiles.interfaces.acl.ProviderContextFacade;
import com.paxtech.utime.platform.reservations.domain.model.commands.DeleteReservationCommand;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetAllReservationsQuery;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetReservationByIdQuery;
import com.paxtech.utime.platform.shared.interfaces.rest.resources.MessageResource;

import com.paxtech.utime.platform.reservations.domain.services.ReservationCommandService;
import com.paxtech.utime.platform.reservations.domain.services.ReservationQueryService;
import com.paxtech.utime.platform.reservations.domain.services.TimeSlotQueryService;
import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.PaymentRepository;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.CreateReservationResource;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.ReservationDetailsResource;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.ReservationResource;
import com.paxtech.utime.platform.reservations.interfaces.rest.transform.CreateReservationCommandFromResourceAssembler;
import com.paxtech.utime.platform.reservations.interfaces.rest.transform.ReservationDetailsResourceFromEntityAssembler;
import com.paxtech.utime.platform.reservations.interfaces.rest.transform.ReservationResourceFromEntityAssembler;
import com.paxtech.utime.platform.services.domain.services.ServiceQueryService;
import com.paxtech.utime.platform.workers.interfaces.rest.acl.WorkerContextFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ReservationController
 */
@RestController
@RequestMapping(value = "/api/v1/reservationsDetails", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reservations", description = "Available Reservation Endpoints")
public class ReservationController {
    private final ReservationCommandService reservationCommandService;
    private final ReservationQueryService reservationQueryService;
    private final ProviderContextFacade providerContextFacade;
    private final TimeSlotQueryService timeSlotQueryService;
    private final WorkerContextFacade workerContextFacade;
    private final ServiceQueryService serviceQueryService;
    private final ClientContextFacade clientContextFacade;
    private final PaymentRepository paymentRepository;

    /**
     * Constructor
     * @param reservationCommandService The {@link ReservationCommandService} instance
     * @param reservationQueryService The {@link ReservationQueryService} instance
     */
    public ReservationController(ReservationCommandService reservationCommandService,
                                 ReservationQueryService reservationQueryService, ProviderContextFacade providerContextFacade, TimeSlotQueryService timeSlotQueryService, WorkerContextFacade workerContextFacade, ServiceQueryService serviceQueryService, ClientContextFacade clientContextFacade, PaymentRepository paymentRepository) {
        this.reservationCommandService = reservationCommandService;
        this.reservationQueryService = reservationQueryService;
        this.providerContextFacade = providerContextFacade;
        this.timeSlotQueryService = timeSlotQueryService;
        this.workerContextFacade = workerContextFacade;
        this.serviceQueryService = serviceQueryService;
        this.clientContextFacade = clientContextFacade;
        this.paymentRepository = paymentRepository;
    }

    /**
     * Create a new reservation
     * @param resource The {@link CreateReservationResource} instance
     * @return A {@link ReservationResource} or a bad request if validation fails
     */
    @PostMapping
    @Operation(summary = "Create a new reservation")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reservation created"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    public ResponseEntity<ReservationResource> createReservation(@RequestBody CreateReservationResource resource) {
        var command = CreateReservationCommandFromResourceAssembler.toCommandFromResource(resource);
        var reservation = reservationCommandService.handle(command);
        if (reservation.isEmpty()) return ResponseEntity.badRequest().build();
        var reservationResource = ReservationResourceFromEntityAssembler.toResourceFromEntity(reservation.get());
        return new ResponseEntity<>(reservationResource, HttpStatus.CREATED);
    }

    /**
     * Get a reservation by ID
     * @param reservationId The reservation ID
     * @return A {@link ReservationResource} or not found if missing
     */
    @GetMapping("/{reservationId}")
    @Operation(summary = "Get a reservation by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation found"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")})
    public ResponseEntity<ReservationResource> getReservationById(@PathVariable Long reservationId) {
        var query = new GetReservationByIdQuery(reservationId);
        var reservation = reservationQueryService.handle(query);
        if (reservation.isEmpty()) return ResponseEntity.notFound().build();
        var reservationResource = ReservationResourceFromEntityAssembler.toResourceFromEntity(reservation.get());
        return ResponseEntity.ok(reservationResource);
    }

    /**
     * Get all reservations
     * @return A list of {@link ReservationResource} or not found if empty
     */
    @GetMapping
    @Operation(summary = "Get all reservations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservations found"),
            @ApiResponse(responseCode = "404", description = "Reservations not found")})
    public ResponseEntity<List<ReservationResource>> getAllReservations() {
        var query = new GetAllReservationsQuery();
        var reservations = reservationQueryService.handle(query);
        var resources = reservations.stream()
                .map(ReservationResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/details/{reservationId}/")
    @Operation(summary = "Get detailed reservation information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation details found"),
            @ApiResponse(responseCode = "404", description = "Reservation not found")})
    public ResponseEntity<ReservationDetailsResource> getReservationDetails(@PathVariable Long reservationId) {
        var query = new GetReservationByIdQuery(reservationId);
        var reservation = reservationQueryService.handle(query);

        if (reservation.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var detailsResource = ReservationDetailsResourceFromEntityAssembler.toResourceFromEntity(
                reservation.get(),
                providerContextFacade,
                timeSlotQueryService,
                workerContextFacade,
                serviceQueryService,
                clientContextFacade,
                paymentRepository
        );

        return ResponseEntity.ok(detailsResource);
    }

    /**
     * Get all reservations with full detail (provider, worker, time-slot, payment)
     * @return 200 + array de ReservationDetailsResource, o 404 si no existen
     */
    @GetMapping("/details")
    @Operation(
            summary = "Get detailed information for all reservations",
            description = "Returns every reservation along with provider, worker, time-slot and payment data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation details found"),
            @ApiResponse(responseCode = "404", description = "No reservations found")
    })
    public ResponseEntity<List<ReservationDetailsResource>> getAllReservationDetails() {

        /* 1. Traer todas las reservas */
        var reservations = reservationQueryService.handle(new GetAllReservationsQuery());

        /* 2. Transformar cada una a ReservationDetailsResource */
        var resources = reservations.stream()
                .map(res -> ReservationDetailsResourceFromEntityAssembler.toResourceFromEntity(
                        res,
                        providerContextFacade,
                        timeSlotQueryService,
                        workerContextFacade,
                        serviceQueryService,
                        clientContextFacade,
                        paymentRepository
                ))
                .toList();

        return ResponseEntity.ok(resources);
    }

    /**
     * Delete a reservation by ID
     * @param reservationId The reservation ID
     * @return 204 No Content if successful, 404 Not Found if reservation doesn't exist
     */
    @DeleteMapping("/{reservationId}")
    @Operation(
            summary = "Delete a reservation",
            description = "Delete a reservation by ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reservation deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Reservation not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<?> deleteReservation(@PathVariable Long reservationId) {
        try {
            var deleteCommand = new DeleteReservationCommand(reservationId);
            reservationCommandService.handle(deleteCommand);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new MessageResource(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new MessageResource("Error deleting reservation: " + e.getMessage()));
        }
    }

}

