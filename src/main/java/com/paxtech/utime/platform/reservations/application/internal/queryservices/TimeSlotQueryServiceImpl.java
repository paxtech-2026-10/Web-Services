package com.paxtech.utime.platform.reservations.application.internal.queryservices;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.TimeSlot;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetAllTimeSlotsQuery;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetTimeSlotByIdQuery;
import com.paxtech.utime.platform.reservations.domain.services.TimeSlotQueryService;
import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.TimeSlotRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotQueryServiceImpl implements TimeSlotQueryService {
    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotQueryServiceImpl(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    @Override
    public Optional<TimeSlot> handle(GetTimeSlotByIdQuery query) {
        return timeSlotRepository.findById(query.Id());
    }

    @Override
    public List<TimeSlot> handle(GetAllTimeSlotsQuery query){
        return timeSlotRepository.findAll();
    }
}
