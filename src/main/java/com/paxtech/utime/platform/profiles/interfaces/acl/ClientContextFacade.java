package com.paxtech.utime.platform.profiles.interfaces.acl;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Client;

import java.util.Optional;

public interface ClientContextFacade {
    Optional<Client> fetchClientById(Long id);
}
