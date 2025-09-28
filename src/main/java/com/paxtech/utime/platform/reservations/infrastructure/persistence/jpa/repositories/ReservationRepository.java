package com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
