package com.paxtech.utime.platform.reservations.domain.services;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.TimeSlot;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetAllTimeSlotsQuery;
import com.paxtech.utime.platform.reservations.domain.model.queries.GetTimeSlotByIdQuery;

import java.util.List;
import java.util.Optional;

public interface TimeSlotQueryService {
    Optional<TimeSlot> handle(GetTimeSlotByIdQuery query);

    List<TimeSlot> handle(GetAllTimeSlotsQuery query);
}
