package com.paxtech.utime.platform.profiles.application.acl;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Client;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.ClientRepository;
import com.paxtech.utime.platform.profiles.interfaces.acl.ClientContextFacade;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientContextFacadeImpl implements ClientContextFacade {
    private final ClientRepository clientRepository;

    public ClientContextFacadeImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Client> fetchClientById(Long id) {
        return clientRepository.findById(id);
    }
}
