package com.paxtech.utime.platform.profiles.interfaces.rest.transform;
/*
import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.ProviderProfile;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.PortfolioInProfile;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.SocialInProfile;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.ProfileResource;

import java.util.List;
import java.util.stream.Collectors;

public class ProfileResourceFromEntityAssembler {

    public static ProfileResource toResourceFromEntity(
            ProviderProfile profile,
            Provider provider,
            List<SocialInProfile> socials,
            List<PortfolioInProfile> portfolios
    ) {
        return new ProfileResource(
                profile.getId(),
                provider.getId().toString(),
                provider.getCompanyName(),
                4.7,
                profile.getProfileUrl().getUrl(),
                profile.getCoverUrl().getUrl(),
                socials.stream().collect(Collectors.toMap(
                        (SocialInProfile s) -> s.getSocial().getSocialIcon(),
                        (SocialInProfile s) -> s.getSocial().getSocialUrl()
                ))
                ,
                portfolios.stream()
                        .map(p -> p.getPortfolioImage().getImageUrl())
                        .collect(Collectors.toList())
        );
    }
}*/
