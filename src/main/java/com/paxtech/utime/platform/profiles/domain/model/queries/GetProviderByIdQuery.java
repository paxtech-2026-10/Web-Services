package com.paxtech.utime.platform.profiles.domain.model.queries;

public record GetProviderByIdQuery(Long id) {
    public GetProviderByIdQuery {
        if (id == null) {
            throw new IllegalArgumentException("Provider id cannot be null");
        }
    }
}
