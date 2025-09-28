package com.paxtech.utime.platform.profiles.domain.model.valueobjects;

public record CompanyName(String value) {

    public CompanyName() {
        this(null);
    }

    public String getValue() {
        return value;
    }
}
