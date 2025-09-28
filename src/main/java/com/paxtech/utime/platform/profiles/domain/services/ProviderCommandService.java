package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.iam.domain.model.aggregates.User;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateProviderCommand;

import java.util.Optional;

public interface ProviderCommandService {

    Optional<Provider> handle(CreateProviderCommand command, User user);
}
