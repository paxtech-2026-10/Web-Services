package com.paxtech.utime.platform.notifications.interfaces.rest.resources;

public record ReminderResource(
        Long id,
        Long reservationId,
        String recipientType,
        Long recipientId,
        String message,
        String appointmentTime,
        boolean read
) {
}
