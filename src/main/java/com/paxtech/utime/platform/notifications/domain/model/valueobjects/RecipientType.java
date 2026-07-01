package com.paxtech.utime.platform.notifications.domain.model.valueobjects;

/**
 * Identifies who a reminder is addressed to: the client that booked the
 * appointment or the provider (salon/barbershop) that will attend it.
 */
public enum RecipientType {
    CLIENT,
    PROVIDER
}
