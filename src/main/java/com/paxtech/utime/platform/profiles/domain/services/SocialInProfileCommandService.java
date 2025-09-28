package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateSocialInProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.SocialInProfile;

import java.util.Optional;

public interface SocialInProfileCommandService {
    Optional<SocialInProfile> handle(CreateSocialInProfileCommand command);

}
