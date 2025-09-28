package com.paxtech.utime.platform.services.domain.services;

import com.paxtech.utime.platform.services.domain.model.aggregates.Service;
import com.paxtech.utime.platform.services.domain.model.commands.CreateServiceCommand;
import com.paxtech.utime.platform.services.domain.model.commands.DeleteServiceCommand;
import com.paxtech.utime.platform.services.domain.model.commands.UpdateServiceCommand;
import com.paxtech.utime.platform.services.domain.model.queries.GetServiceByIdQuery;

import java.util.Optional;

public interface ServiceCommandService {
    Optional<Service> handle(CreateServiceCommand command);

    void handle(DeleteServiceCommand command);

    Optional<Service> handle(UpdateServiceCommand command);
}
