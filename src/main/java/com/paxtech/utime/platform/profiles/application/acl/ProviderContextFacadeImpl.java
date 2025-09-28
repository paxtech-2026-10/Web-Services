package com.paxtech.utime.platform.profiles.application.acl;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.ProviderRepository;
import com.paxtech.utime.platform.profiles.interfaces.acl.ProviderContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ProviderContextFacadeImpl implements ProviderContextFacade {
    private final ProviderRepository providerRepository;

    public ProviderContextFacadeImpl(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @Override
    public Optional<Provider> fetchProviderById(Long id) {
        return providerRepository.findById(id);
    }
}
