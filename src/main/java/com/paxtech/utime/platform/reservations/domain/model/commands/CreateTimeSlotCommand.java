package com.paxtech.utime.platform.reservations.domain.model.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record CreateTimeSlotCommand(
        LocalDateTime startTime,
        LocalDateTime endTime,
        boolean status,
        String type
) {
    public CreateTimeSlotCommand {
        if (startTime == null) {
            throw new IllegalArgumentException("startTime cannot be null");
        }
        // No permitir reservar en fechas pasadas. Se compara a nivel de día para
        // evitar falsos rechazos por diferencias de zona horaria entre cliente y servidor.
        if (startTime.toLocalDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("No se puede reservar en una fecha pasada");
        }
        if (endTime != null && endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("endTime cannot be before startTime");
        }
    }
}
