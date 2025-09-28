package com.paxtech.utime.platform.reservations.application.internal.commandservices;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.Reservation;
import com.paxtech.utime.platform.reservations.domain.model.commands.CreateReservationCommand;
import com.paxtech.utime.platform.reservations.domain.services.ReservationCommandService;
import com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReservationCommandServiceImpl implements ReservationCommandService {

    private final ReservationRepository repository;

    public ReservationCommandServiceImpl(ReservationRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Reservation> handle(CreateReservationCommand command) {
        if (command.clientId() == null || command.clientId() <= 0 ||
                command.providerId() == null || command.providerId() <= 0 ||
                command.paymentId() == null || command.paymentId() <= 0 ||
                command.timeSlotId() == null || command.timeSlotId()<=0 ||
                command.workerId() == null || command.workerId() <= 0) {
            throw new IllegalArgumentException("Invalid reservation command");
        }
        var reservation = new Reservation(command);
        repository.save(reservation);
        return Optional.of(reservation);
    }
}
