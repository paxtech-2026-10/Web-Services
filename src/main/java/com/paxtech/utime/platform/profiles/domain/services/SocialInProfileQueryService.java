package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.SocialInProfile;
import com.paxtech.utime.platform.profiles.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface SocialInProfileQueryService {
    List<SocialInProfile> handle(GetAllSocialsInProfilesQuery query);
    Optional<SocialInProfile> handle(GetSocialInProfileByIdQuery query);
    List<SocialInProfile> handle(GetSocialsInProfileByProviderProfileIdQuery query);
    List<SocialInProfile> handle(GetAllSocialsInProfileByProviderProfileIdQuery query);
}
