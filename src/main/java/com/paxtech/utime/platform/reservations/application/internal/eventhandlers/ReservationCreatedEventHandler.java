package com.paxtech.utime.platform.reservations.application.internal.eventhandlers;

import com.paxtech.utime.platform.reservations.domain.model.events.ReservationCreatedEvent;
import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.TimeSlotRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
public class ReservationCreatedEventHandler {

    private final TimeSlotRepository timeSlotRepository;

    public ReservationCreatedEventHandler(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void on(ReservationCreatedEvent event) {
        var timeSlot = timeSlotRepository.findById(event.timeSlotId())
                .orElseThrow(() -> new IllegalArgumentException("Time slot with this id does not exist"));
        timeSlot.markUnavailable();
        timeSlotRepository.save(timeSlot);
    }
}
