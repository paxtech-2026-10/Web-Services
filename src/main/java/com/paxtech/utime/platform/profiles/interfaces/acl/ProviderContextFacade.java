package com.paxtech.utime.platform.profiles.interfaces.acl;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;

import java.util.Optional;

public interface ProviderContextFacade {
    Optional<Provider> fetchProviderById(Long id);
}
