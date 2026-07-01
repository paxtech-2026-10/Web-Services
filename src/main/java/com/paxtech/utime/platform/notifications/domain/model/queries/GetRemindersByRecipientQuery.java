package com.paxtech.utime.platform.notifications.domain.model.queries;

import com.paxtech.utime.platform.notifications.domain.model.valueobjects.RecipientType;

public record GetRemindersByRecipientQuery(RecipientType recipientType, Long recipientId) {
}
