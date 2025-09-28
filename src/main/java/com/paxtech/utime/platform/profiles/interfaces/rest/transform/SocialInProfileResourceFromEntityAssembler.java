package com.paxtech.utime.platform.profiles.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.SocialInProfile;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.SocialInProfileResource;

public class SocialInProfileResourceFromEntityAssembler {
    public static SocialInProfileResource toResourceFromEntity(SocialInProfile entity) {
        return new SocialInProfileResource(entity.getId(), entity.getProviderProfile().getId(), entity.getSocial().getId());
    }
}
