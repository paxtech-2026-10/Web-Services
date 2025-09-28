package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.ProviderProfile;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllProviderProfilesQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderProfileByIdQuery;

import java.util.List;
import java.util.Optional;

public interface ProviderProfileQueryService {
    Optional<ProviderProfile> handle(GetProviderProfileByIdQuery query);

    List<ProviderProfile> handle(GetAllProviderProfilesQuery query);
}
