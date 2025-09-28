package com.paxtech.utime.platform.reservations.interfaces.rest;

import com.paxtech.utime.platform.reservations.domain.model.commands.CreateTimeSlotCommand;
import com.paxtech.utime.platform.reservations.domain.services.TimeSlotCommandService;
import com.paxtech.utime.platform.reservations.domain.services.TimeSlotQueryService;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.CreateTimeSlotResource;
import com.paxtech.utime.platform.reservations.interfaces.rest.resources.TimeSlotResource;
import com.paxtech.utime.platform.reservations.interfaces.rest.transform.CreateTimeSlotCommandFromResourceAssembler;
import com.paxtech.utime.platform.reservations.interfaces.rest.transform.TimeSlotResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TimeSlotController
 *
 * REST controller for managing time slots.
 * Provides endpoints to create, retrieve by ID, and list all time slots.
 */
@RestController
@RequestMapping(value = "/api/v1/time-slots", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Time Slots", description = "Available Time Slots Endpoints")
public class TimeSlotController {

    private final TimeSlotCommandService timeSlotCommandService;
    private final TimeSlotQueryService timeSlotQueryService;

    /**
     * Constructor
     * @param timeSlotCommandService Service responsible for handling time slot creation
     * @param timeSlotQueryService Service responsible for querying time slots
     */
    public TimeSlotController(TimeSlotCommandService timeSlotCommandService, TimeSlotQueryService timeSlotQueryService) {
        this.timeSlotCommandService = timeSlotCommandService;
        this.timeSlotQueryService = timeSlotQueryService;
    }

    /**
     * Creates a new time slot
     * @param resource The {@link CreateTimeSlotResource} with the required time slot data
     * @return A {@link TimeSlotResource} if successful, or 400 Bad Request if creation fails
     */
    @PostMapping
    @Operation(summary = "Create a new Time Slot")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "TimeSlot created"),
            @ApiResponse(responseCode = "400", description = "Bad request")
    })
    public ResponseEntity<TimeSlotResource> createTimeSlot(@RequestBody CreateTimeSlotResource resource) {
        var createTimeSlotCommand = CreateTimeSlotCommandFromResourceAssembler.toCommandFromResource(resource);
        var timeSlot = timeSlotCommandService.handle(createTimeSlotCommand);
        if (timeSlot.isEmpty()) return ResponseEntity.badRequest().build();
        var createdTimeSlot = timeSlot.get();
        var timeSlotResource = TimeSlotResourceFromEntityAssembler.toResourceFromEntity(createdTimeSlot);
        return new ResponseEntity<>(timeSlotResource, HttpStatus.CREATED);
    }

    /**
     * Retrieves a time slot by its ID
     * @param id The ID of the time slot
     * @return A {@link TimeSlotResource} if found, or 404 Not Found if it doesn't exist
     */
    @GetMapping("{id}")
    @Operation(summary = "Get a Time Slot by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TimeSlot found"),
            @ApiResponse(responseCode = "404", description = "TimeSlot not found")
    })
    public ResponseEntity<TimeSlotResource> getTimeSlotById(@PathVariable Long id) {
        var query = new com.paxtech.utime.platform.reservations.domain.model.queries.GetTimeSlotByIdQuery(id);
        var result = timeSlotQueryService.handle(query);
        if (result.isEmpty()) return ResponseEntity.notFound().build();
        var resource = TimeSlotResourceFromEntityAssembler.toResourceFromEntity(result.get());
        return ResponseEntity.ok(resource);
    }

    /**
     * Retrieves all time slots
     * @return A list of {@link TimeSlotResource}, or 404 Not Found if no time slots exist
     */
    @GetMapping
    @Operation(summary = "Get all Time Slots")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "TimeSlots found"),
            @ApiResponse(responseCode = "404", description = "TimeSlots not found")
    })
    public ResponseEntity<Iterable<TimeSlotResource>> getAllTimeSlots() {
        var timeSlots = timeSlotQueryService.handle(
                new com.paxtech.utime.platform.reservations.domain.model.queries.GetAllTimeSlotsQuery());
        if (timeSlots.isEmpty()) return ResponseEntity.notFound().build();
        var resources = timeSlots.stream()
                .map(TimeSlotResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
}
