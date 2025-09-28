package com.paxtech.utime.platform.services.domain.model.valueobjects;

public record Duration(Integer duration) {
    public Duration() {
        this(null);
    }
}
