package com.paxtech.utime.platform.services.domain.model.valueobjects;

public record Status(boolean status) {
    public Status(){
        this(true);
    }
}
