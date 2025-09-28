package com.paxtech.utime.platform.reservations.interfaces.rest.resources;

import java.time.DateTimeException;
import java.time.LocalDateTime;

public record CreateTimeSlotResource(LocalDateTime startTime, LocalDateTime endTime, boolean status, String type) {
}
