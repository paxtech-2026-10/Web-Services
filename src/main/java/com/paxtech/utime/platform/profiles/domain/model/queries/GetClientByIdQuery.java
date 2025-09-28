package com.paxtech.utime.platform.profiles.domain.model.queries;

public record GetClientByIdQuery(Long id) {
    public GetClientByIdQuery {
        if (id == null) {
            throw new IllegalArgumentException("Client id cannot be null");
        }
    }
}