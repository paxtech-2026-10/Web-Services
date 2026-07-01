package com.paxtech.utime.platform.notifications.infrastructure.persistence.jpa.repositories;

import com.paxtech.utime.platform.notifications.domain.model.aggregates.Reminder;
import com.paxtech.utime.platform.notifications.domain.model.valueobjects.RecipientType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    boolean existsByReservationIdAndRecipientType(Long reservationId, RecipientType recipientType);

    List<Reminder> findByRecipientTypeAndRecipientIdOrderByAppointmentTimeAsc(RecipientType recipientType, Long recipientId);
}
