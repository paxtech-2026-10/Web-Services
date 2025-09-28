package com.paxtech.utime.platform.reservations.domain.services;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.TimeSlot;
import com.paxtech.utime.platform.reservations.domain.model.commands.CreateTimeSlotCommand;

import java.util.Optional;

public interface TimeSlotCommandService {
    Optional<TimeSlot> handle(CreateTimeSlotCommand command);
}
