package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.iam.domain.model.aggregates.User;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.Client;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateClientCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateClientCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeleteClientCommand;

import java.util.Optional;


public interface ClientCommandService {

    Optional<Client> handle(CreateClientCommand command, User user);
    
    Optional<Client> handle(UpdateClientCommand command);
    
    void handle(DeleteClientCommand command);
}
