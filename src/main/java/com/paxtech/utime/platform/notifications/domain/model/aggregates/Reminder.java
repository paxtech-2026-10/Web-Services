package com.paxtech.utime.platform.notifications.domain.model.aggregates;

import com.paxtech.utime.platform.notifications.domain.model.commands.CreateReminderCommand;
import com.paxtech.utime.platform.notifications.domain.model.valueobjects.RecipientType;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * In-app automatic reminder generated for an upcoming appointment (UE02).
 * One reminder is created per recipient (client and provider) for each
 * reservation whose appointment starts within the next 24 hours.
 */
@Entity
public class Reminder extends AuditableAbstractAggregateRoot<Reminder> {

    @Column(nullable = false)
    @Getter
    @Setter
    private Long reservationId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Getter
    @Setter
    private RecipientType recipientType;

    @Column(nullable = false)
    @Getter
    @Setter
    private Long recipientId;

    @Column(nullable = false, length = 500)
    @Getter
    @Setter
    private String message;

    @Column(nullable = false)
    @Getter
    @Setter
    private LocalDateTime appointmentTime;

    @Column(nullable = false)
    @Getter
    @Setter
    private boolean isRead;

    public Reminder() {}

    public Reminder(CreateReminderCommand command) {
        this.reservationId = command.reservationId();
        this.recipientType = command.recipientType();
        this.recipientId = command.recipientId();
        this.message = command.message();
        this.appointmentTime = command.appointmentTime();
        this.isRead = false;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
