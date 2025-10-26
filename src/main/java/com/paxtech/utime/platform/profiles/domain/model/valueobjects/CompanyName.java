package com.paxtech.utime.platform.profiles.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record CompanyName(String value) {

    public CompanyName() {
        this(null);
    }

    public String getValue() {
        return value;
    }
}
