package com.paxtech.utime.platform.analytics.interfaces.rest;

import com.paxtech.utime.platform.analytics.domain.model.queries.GetAllAnalyticsEventsQuery;
import com.paxtech.utime.platform.analytics.domain.model.queries.GetAnalyticsEventByIdQuery;
import com.paxtech.utime.platform.analytics.domain.model.queries.GetAnalyticsSummaryQuery;
import com.paxtech.utime.platform.analytics.domain.model.valueobjects.ActorType;
import com.paxtech.utime.platform.analytics.domain.services.AnalyticsEventCommandService;
import com.paxtech.utime.platform.analytics.domain.services.AnalyticsEventQueryService;
import com.paxtech.utime.platform.analytics.interfaces.rest.resources.AnalyticsEventResource;
import com.paxtech.utime.platform.analytics.interfaces.rest.resources.EventTypeCountResource;
import com.paxtech.utime.platform.analytics.interfaces.rest.resources.RecordAnalyticsEventResource;
import com.paxtech.utime.platform.analytics.interfaces.rest.transform.AnalyticsEventResourceFromEntityAssembler;
import com.paxtech.utime.platform.analytics.interfaces.rest.transform.EventTypeCountResourceFromEntityAssembler;
import com.paxtech.utime.platform.analytics.interfaces.rest.transform.RecordAnalyticsEventCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * Public analytics/tracking endpoints (UE01-UE05).
 * <p>
 * These endpoints are intentionally open (no authentication required) so both
 * the mobile clients can push tracking events and a separate analytics frontend
 * can read the collected data. Every recorded event keeps track of which
 * client/provider (actor) triggered the action.
 */
@RestController
@RequestMapping(value = "/api/v1/analytics", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Analytics", description = "Public experiment tracking events (UE01-UE05)")
public class AnalyticsController {

    private final AnalyticsEventCommandService analyticsEventCommandService;
    private final AnalyticsEventQueryService analyticsEventQueryService;

    public AnalyticsController(AnalyticsEventCommandService analyticsEventCommandService,
                              AnalyticsEventQueryService analyticsEventQueryService) {
        this.analyticsEventCommandService = analyticsEventCommandService;
        this.analyticsEventQueryService = analyticsEventQueryService;
    }

    @PostMapping("/events")
    @Operation(summary = "Record an analytics event",
            description = "Public endpoint. Registers a tracking event (e.g. calendar_viewed, " +
                    "book_in_app_completed, profile_view) together with the actor that triggered it.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Event recorded"),
            @ApiResponse(responseCode = "400", description = "Invalid event")})
    public ResponseEntity<AnalyticsEventResource> recordEvent(@RequestBody RecordAnalyticsEventResource resource) {
        var command = RecordAnalyticsEventCommandFromResourceAssembler.toCommandFromResource(resource);
        var event = analyticsEventCommandService.handle(command);
        var eventResource = AnalyticsEventResourceFromEntityAssembler.toResourceFromEntity(event);
        return new ResponseEntity<>(eventResource, HttpStatus.CREATED);
    }

    @GetMapping("/events")
    @Operation(summary = "List analytics events",
            description = "Public endpoint. Returns recorded events (newest first). " +
                    "Optionally filtered by eventType, actorType, actorId and providerId.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Events found")})
    public ResponseEntity<List<AnalyticsEventResource>> getEvents(
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String actorType,
            @RequestParam(required = false) Long actorId,
            @RequestParam(required = false) Long providerId) {
        var query = new GetAllAnalyticsEventsQuery(
                eventType,
                actorType != null ? ActorType.fromString(actorType) : null,
                actorId,
                providerId);
        var resources = analyticsEventQueryService.handle(query).stream()
                .map(AnalyticsEventResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/events/{eventId}")
    @Operation(summary = "Get an analytics event by id", description = "Public endpoint.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Event found"),
            @ApiResponse(responseCode = "404", description = "Event not found")})
    public ResponseEntity<AnalyticsEventResource> getEventById(@PathVariable Long eventId) {
        var event = analyticsEventQueryService.handle(new GetAnalyticsEventByIdQuery(eventId));
        return event
                .map(AnalyticsEventResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/summary")
    @Operation(summary = "Get event counts by type",
            description = "Public endpoint. Returns the total number of recorded events grouped " +
                    "by event type, to feed the analytics dashboard.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Summary computed")})
    public ResponseEntity<List<EventTypeCountResource>> getSummary() {
        var resources = analyticsEventQueryService.handle(new GetAnalyticsSummaryQuery()).stream()
                .map(EventTypeCountResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }
}
