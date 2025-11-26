package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.ProviderProfile;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllProviderProfilesQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderProfileByIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderProfileByProviderIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderProfilesByCompanyNameQuery;

import java.util.List;
import java.util.Optional;

public interface ProviderProfileQueryService {
    Optional<ProviderProfile> handle(GetProviderProfileByIdQuery query);

    Optional<ProviderProfile> handle(GetProviderProfileByProviderIdQuery query);

    List<ProviderProfile> handle(GetAllProviderProfilesQuery query);

    List<ProviderProfile> handle(GetProviderProfilesByCompanyNameQuery query);
}
