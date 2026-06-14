package com.paxtech.utime.platform.reservations.application.internal.eventhandlers;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.TimeSlot;
import com.paxtech.utime.platform.reservations.domain.model.commands.CreateReservationCommand;
import com.paxtech.utime.platform.reservations.domain.model.commands.CreateTimeSlotCommand;
import com.paxtech.utime.platform.reservations.domain.services.ReservationCommandService;
import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.ReservationRepository;
import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.TimeSlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReservationCreatedEventHandlerIntegrationTest {

    @Autowired
    private ReservationCommandService reservationCommandService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @BeforeEach
    void cleanDatabase() {
        reservationRepository.deleteAll();
        timeSlotRepository.deleteAll();
    }

    @Test
    @DisplayName("Creating a reservation marks the reserved time slot unavailable")
    void createReservation_marksTimeSlotUnavailable() {
        var slotStart = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);
        var availableTimeSlot = timeSlotRepository.save(new TimeSlot(new CreateTimeSlotCommand(
                slotStart,
                slotStart.plusMinutes(30),
                true,
                "REGULAR"
        )));

        reservationCommandService.handle(new CreateReservationCommand(
                1L,
                1L,
                1L,
                availableTimeSlot.getId(),
                1L
        ));

        var updatedTimeSlot = timeSlotRepository.findById(availableTimeSlot.getId());

        assertThat(updatedTimeSlot).isPresent();
        assertThat(updatedTimeSlot.get().isStatus()).isFalse();
    }
}
