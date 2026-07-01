package com.paxtech.utime.platform.notifications.interfaces.rest;

import com.paxtech.utime.platform.notifications.domain.model.commands.MarkReminderReadCommand;
import com.paxtech.utime.platform.notifications.domain.model.queries.GetRemindersByRecipientQuery;
import com.paxtech.utime.platform.notifications.domain.model.valueobjects.RecipientType;
import com.paxtech.utime.platform.notifications.domain.services.ReminderCommandService;
import com.paxtech.utime.platform.notifications.domain.services.ReminderQueryService;
import com.paxtech.utime.platform.notifications.interfaces.rest.resources.ReminderResource;
import com.paxtech.utime.platform.notifications.interfaces.rest.transform.ReminderResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/notifications", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Notifications", description = "Automatic appointment reminders (UE02)")
public class NotificationsController {

    private final ReminderQueryService reminderQueryService;
    private final ReminderCommandService reminderCommandService;

    public NotificationsController(ReminderQueryService reminderQueryService,
                                   ReminderCommandService reminderCommandService) {
        this.reminderQueryService = reminderQueryService;
        this.reminderCommandService = reminderCommandService;
    }

    @GetMapping("/provider/{providerId}")
    @Operation(summary = "Get provider reminders", description = "Get all reminders addressed to a provider")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Reminders found")})
    public ResponseEntity<List<ReminderResource>> getProviderReminders(@PathVariable Long providerId) {
        var reminders = reminderQueryService.handle(
                new GetRemindersByRecipientQuery(RecipientType.PROVIDER, providerId));
        var resources = reminders.stream()
                .map(ReminderResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get client reminders", description = "Get all reminders addressed to a client")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Reminders found")})
    public ResponseEntity<List<ReminderResource>> getClientReminders(@PathVariable Long clientId) {
        var reminders = reminderQueryService.handle(
                new GetRemindersByRecipientQuery(RecipientType.CLIENT, clientId));
        var resources = reminders.stream()
                .map(ReminderResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(resources);
    }

    @PatchMapping("/{reminderId}/read")
    @Operation(summary = "Mark reminder as read", description = "Mark a reminder as read")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reminder marked as read"),
            @ApiResponse(responseCode = "404", description = "Reminder not found")})
    public ResponseEntity<?> markReminderAsRead(@PathVariable Long reminderId) {
        reminderCommandService.handle(new MarkReminderReadCommand(reminderId));
        return ResponseEntity.ok("Reminder with given id successfully marked as read");
    }
}
