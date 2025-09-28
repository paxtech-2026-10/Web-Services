package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.ProviderProfile;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateProviderProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeleteProviderProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateProviderProfileCommand;

import java.util.Optional;

public interface ProviderProfileCommandService {
    Optional<ProviderProfile> handle(CreateProviderProfileCommand command);

    Optional<ProviderProfile> handle(UpdateProviderProfileCommand command);

    void handle(DeleteProviderProfileCommand command);
}
