package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Social;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllSocialsQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetSocialByIconQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetSocialByIdQuery;

import java.util.List;
import java.util.Optional;

public interface SocialQueryService {
    List<Social> handle(GetAllSocialsQuery query);
    Optional<Social> handle(GetSocialByIdQuery query);
    Optional<Social> handle(GetSocialByIconQuery query);
}
