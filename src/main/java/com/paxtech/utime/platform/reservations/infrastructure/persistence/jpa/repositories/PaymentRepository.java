package com.paxtech.utime.platform.reservations.infrastructure.persistence.jpa.repositories;

import com.paxtech.utime.platform.reservations.domain.model.aggregates.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByReservationId(Long reservationId);
}
