package com.paxtech.utime.platform.profiles.domain.model.queries;

public record GetProviderProfileByProviderIdQuery(Long providerId) {
    public GetProviderProfileByProviderIdQuery {
        if (providerId == null) {
            throw new NullPointerException("providerId is null");
        }
    }
}


