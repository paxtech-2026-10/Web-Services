package com.paxtech.utime.platform.notifications.interfaces.rest.transform;

import com.paxtech.utime.platform.notifications.domain.model.aggregates.Reminder;
import com.paxtech.utime.platform.notifications.interfaces.rest.resources.ReminderResource;

public class ReminderResourceFromEntityAssembler {
    public static ReminderResource toResourceFromEntity(Reminder entity) {
        return new ReminderResource(
                entity.getId(),
                entity.getReservationId(),
                entity.getRecipientType().name(),
                entity.getRecipientId(),
                entity.getMessage(),
                entity.getAppointmentTime().toString(),
                entity.isRead());
    }
}
