package com.paxtech.utime.platform.notifications.application.internal.commandservices;

import com.paxtech.utime.platform.notifications.domain.model.aggregates.Reminder;
import com.paxtech.utime.platform.notifications.domain.model.commands.CreateReminderCommand;
import com.paxtech.utime.platform.notifications.domain.model.commands.MarkReminderReadCommand;
import com.paxtech.utime.platform.notifications.domain.services.ReminderCommandService;
import com.paxtech.utime.platform.notifications.infrastructure.persistence.jpa.repositories.ReminderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReminderCommandServiceImpl implements ReminderCommandService {
    private final ReminderRepository reminderRepository;

    public ReminderCommandServiceImpl(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    @Override
    public Optional<Reminder> handle(CreateReminderCommand command) {
        // Idempotency: only one reminder per reservation and recipient.
        if (reminderRepository.existsByReservationIdAndRecipientType(command.reservationId(), command.recipientType())) {
            return Optional.empty();
        }
        var reminder = new Reminder(command);
        reminderRepository.save(reminder);
        return Optional.of(reminder);
    }

    @Override
    public void handle(MarkReminderReadCommand command) {
        var reminder = reminderRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Reminder does not exist"));
        reminder.markAsRead();
        reminderRepository.save(reminder);
    }
}
