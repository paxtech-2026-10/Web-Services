package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.iam.domain.model.aggregates.User;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.Client;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateClientCommand;

import java.util.Optional;


public interface ClientCommandService {

    Optional<Client> handle(CreateClientCommand command, User user);
}
