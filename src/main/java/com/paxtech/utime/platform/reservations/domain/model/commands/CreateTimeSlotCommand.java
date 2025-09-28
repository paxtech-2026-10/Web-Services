package com.paxtech.utime.platform.reservations.domain.model.commands;

import java.time.LocalDateTime;

public record CreateTimeSlotCommand(
        LocalDateTime startTime,
        LocalDateTime endTime,
        boolean status,
        String type
) {}
