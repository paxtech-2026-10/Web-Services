package com.paxtech.utime.platform.notifications.domain.services;

import com.paxtech.utime.platform.notifications.domain.model.aggregates.Reminder;
import com.paxtech.utime.platform.notifications.domain.model.queries.GetRemindersByRecipientQuery;

import java.util.List;

public interface ReminderQueryService {
    List<Reminder> handle(GetRemindersByRecipientQuery query);
}
