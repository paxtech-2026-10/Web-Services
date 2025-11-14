package com.paxtech.utime.platform.profiles.application.internal.commandservices;

import com.paxtech.utime.platform.iam.domain.model.aggregates.User;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.Client;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateClientCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateClientCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeleteClientCommand;
import com.paxtech.utime.platform.profiles.domain.services.ClientCommandService;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.ClientRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientCommandServiceImpl implements ClientCommandService {
    private final ClientRepository clientRepository;

    public ClientCommandServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Client> handle(CreateClientCommand command, User user) {

        var client = new Client(command, user);
        var saved = clientRepository.save(client);
        return Optional.of(saved);
    }

    @Override
    public Optional<Client> handle(UpdateClientCommand command) {
        var clientOptional = clientRepository.findById(command.id());
        if (clientOptional.isEmpty()) {
            return Optional.empty();
        }
        
        var client = clientOptional.get();
        
        // Solo actualizar firstName y lastName si no son null
        if (command.firstName() != null && command.lastName() != null) {
            client.updateFullName(command.firstName(), command.lastName());
        }
        
        // Solo actualizar profileImageUrl si no es null
        if (command.profileImageUrl() != null) {
            client.updateProfileImageUrl(command.profileImageUrl());
        }
        
        var updated = clientRepository.save(client);
        return Optional.of(updated);
    }

    @Override
    public void handle(DeleteClientCommand command) {
        if (!clientRepository.existsById(command.id())) {
            throw new IllegalArgumentException("Client with ID " + command.id() + " does not exist");
        }
        try {
            clientRepository.deleteById(command.id());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting client", e);
        }
    }


}
