package com.paxtech.utime.platform.services.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Name(String name) {
    public Name(){
        this(null);
    }
}
