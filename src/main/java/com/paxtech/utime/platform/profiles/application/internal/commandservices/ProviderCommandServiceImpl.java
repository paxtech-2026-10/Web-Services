package com.paxtech.utime.platform.profiles.application.internal.commandservices;

import com.paxtech.utime.platform.iam.domain.model.aggregates.User;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateProviderCommand;
import com.paxtech.utime.platform.profiles.domain.services.ProviderCommandService;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProviderCommandServiceImpl implements ProviderCommandService {
    private final ProviderRepository providerRepository;

    public ProviderCommandServiceImpl(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @Override
    public Optional<Provider> handle(CreateProviderCommand command, User user) {
        var provider = new Provider(command, user);
        var saved = providerRepository.save(provider);
        return Optional.of(saved);
    }

}
