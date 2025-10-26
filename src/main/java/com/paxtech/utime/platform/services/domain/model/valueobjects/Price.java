package com.paxtech.utime.platform.services.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Price(Long price) {
}
