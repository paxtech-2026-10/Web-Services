package com.paxtech.utime.platform.reservations.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Money(float amount, String currency) {
}
