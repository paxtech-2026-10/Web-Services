package com.paxtech.utime.platform.reservations.interfaces.rest.resources;

import java.time.LocalDateTime;

public record TimeSlotResource(Long id, LocalDateTime startTime, LocalDateTime endTime, boolean status, String type) {
}
