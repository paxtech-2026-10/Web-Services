package com.paxtech.utime.platform.notifications.domain.services;

import com.paxtech.utime.platform.notifications.domain.model.aggregates.Reminder;
import com.paxtech.utime.platform.notifications.domain.model.commands.CreateReminderCommand;
import com.paxtech.utime.platform.notifications.domain.model.commands.MarkReminderReadCommand;

import java.util.Optional;

public interface ReminderCommandService {
    Optional<Reminder> handle(CreateReminderCommand command);

    void handle(MarkReminderReadCommand command);
}
