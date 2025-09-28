package com.paxtech.utime.platform.profiles.domain.model.queries;

public record GetProviderProfileByIdQuery(Long id) {
    public GetProviderProfileByIdQuery {
        if (id == null) {
            throw new NullPointerException("id is null");
        }
    }
}
