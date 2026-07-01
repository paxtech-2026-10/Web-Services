package com.paxtech.utime.platform.notifications.domain.model.commands;

import com.paxtech.utime.platform.notifications.domain.model.valueobjects.RecipientType;

import java.time.LocalDateTime;

public record CreateReminderCommand(
        Long reservationId,
        RecipientType recipientType,
        Long recipientId,
        String message,
        LocalDateTime appointmentTime
) {
}
