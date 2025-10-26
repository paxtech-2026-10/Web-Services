package com.paxtech.utime.platform.profiles.application.internal.commandservices;

import com.paxtech.utime.platform.iam.domain.model.aggregates.User;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateProviderCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateProviderCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeleteProviderCommand;
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

    @Override
    public Optional<Provider> handle(UpdateProviderCommand command) {
        var providerOptional = providerRepository.findById(command.id());
        if (providerOptional.isEmpty()) {
            return Optional.empty();
        }
        
        var provider = providerOptional.get();
        provider.updateCompanyName(command.companyName());
        var updated = providerRepository.save(provider);
        return Optional.of(updated);
    }

    @Override
    public void handle(DeleteProviderCommand command) {
        if (!providerRepository.existsById(command.id())) {
            throw new IllegalArgumentException("Provider with ID " + command.id() + " does not exist");
        }
        try {
            providerRepository.deleteById(command.id());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting provider", e);
        }
    }

}
