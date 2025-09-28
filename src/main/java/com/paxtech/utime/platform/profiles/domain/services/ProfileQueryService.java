package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.SocialInProfile;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.PortfolioInProfile;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetSocialsByProfileIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetPortfoliosByProfileIdQuery;

import java.util.List;

public interface ProfileQueryService {
    List<SocialInProfile> handle(GetSocialsByProfileIdQuery query);
    List<PortfolioInProfile> handle(GetPortfoliosByProfileIdQuery query);
}
