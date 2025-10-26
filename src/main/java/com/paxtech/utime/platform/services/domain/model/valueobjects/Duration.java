package com.paxtech.utime.platform.services.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Duration(Integer duration) {
    public Duration() {
        this(null);
    }
}
