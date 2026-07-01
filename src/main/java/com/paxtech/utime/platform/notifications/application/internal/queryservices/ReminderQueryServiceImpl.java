package com.paxtech.utime.platform.notifications.application.internal.queryservices;

import com.paxtech.utime.platform.notifications.domain.model.aggregates.Reminder;
import com.paxtech.utime.platform.notifications.domain.model.queries.GetRemindersByRecipientQuery;
import com.paxtech.utime.platform.notifications.domain.services.ReminderQueryService;
import com.paxtech.utime.platform.notifications.infrastructure.persistence.jpa.repositories.ReminderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderQueryServiceImpl implements ReminderQueryService {
    private final ReminderRepository reminderRepository;

    public ReminderQueryServiceImpl(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    @Override
    public List<Reminder> handle(GetRemindersByRecipientQuery query) {
        return reminderRepository.findByRecipientTypeAndRecipientIdOrderByAppointmentTimeAsc(
                query.recipientType(), query.recipientId());
    }
}
