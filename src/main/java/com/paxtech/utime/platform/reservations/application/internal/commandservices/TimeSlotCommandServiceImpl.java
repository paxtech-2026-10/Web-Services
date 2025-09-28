package com.paxtech.utime.platform.reservations.application.internal.commandservices;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.TimeSlot;
import com.paxtech.utime.platform.reservations.domain.model.commands.CreateTimeSlotCommand;
import com.paxtech.utime.platform.reservations.domain.services.TimeSlotCommandService;
import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TimeSlotCommandServiceImpl implements TimeSlotCommandService {

    private final TimeSlotRepository repository;

    public TimeSlotCommandServiceImpl(TimeSlotRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<TimeSlot> handle(CreateTimeSlotCommand command) {
        var timeSlot = new TimeSlot(command);
        var saved = repository.save(timeSlot);
        return Optional.of(saved);
    }
}
