package com.paxtech.utime.platform.services.application.internal.commandservices;

import com.paxtech.utime.platform.services.domain.model.aggregates.Service;
import com.paxtech.utime.platform.services.domain.model.commands.CreateServiceCommand;
import com.paxtech.utime.platform.services.domain.model.commands.DeleteServiceCommand;
import com.paxtech.utime.platform.services.domain.model.commands.UpdateServiceCommand;
import com.paxtech.utime.platform.services.domain.services.ServiceCommandService;
import com.paxtech.utime.platform.services.infrastructure.persistence.jpa.repositories.ServiceRepository;

import java.util.Optional;
@org.springframework.stereotype.Service
public class ServiceCommandServiceImpl implements ServiceCommandService {
    private final ServiceRepository serviceRepository;

    public ServiceCommandServiceImpl(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    @Override
    public Optional<Service> handle(CreateServiceCommand command) {
        var service = new Service(command);
        if (serviceRepository.existsByProviderIdAndName(service.getProviderId(), service.getName())) {
            throw new IllegalArgumentException("A service with this name already exists for this salon");
        }
        serviceRepository.save(service);
        return Optional.of(service);
    }

    @Override
    public void handle(DeleteServiceCommand command) {
        if(!serviceRepository.existsById(command.id())){
            throw new IllegalArgumentException("Service with this id does not exist");
        }
        try {
            serviceRepository.deleteById(command.id());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting service", e);
        }
    }

    @Override
    public Optional<Service> handle(UpdateServiceCommand command) {
        var serviceToUpdate = serviceRepository.findById(command.id())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Service with id " + command.id() + " does not exist"));
        serviceToUpdate.updateInformation(command.name(), command.duration(), command.price());
        return Optional.of(serviceRepository.save(serviceToUpdate));
    }
}
