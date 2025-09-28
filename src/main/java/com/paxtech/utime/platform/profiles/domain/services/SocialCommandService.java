package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Social;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateSocialCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeleteSocialCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateSocialCommand;

import java.util.Optional;

public interface SocialCommandService {
    Optional<Social> handle(CreateSocialCommand command);
    Optional<Social> handle(UpdateSocialCommand command);
    void handle(DeleteSocialCommand command);
}
