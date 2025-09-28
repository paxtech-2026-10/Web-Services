package com.paxtech.utime.platform.services.domain.model.valueobjects;

public record Name(String name) {
    public Name(){
        this(null);
    }
}
