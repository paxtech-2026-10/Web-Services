package com.paxtech.utime.platform.profiles.application.internal.commandservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.ProviderProfile;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateProviderProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeleteProviderProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateProviderProfileCommand;
import com.paxtech.utime.platform.profiles.domain.services.ProviderProfileCommandService;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.ProviderProfileRepository;

import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ProviderProfileCommandServiceImpl implements ProviderProfileCommandService {
    private final ProviderProfileRepository providerProfileRepository;
    public ProviderProfileCommandServiceImpl(ProviderProfileRepository providerProfileRepository) {
        this.providerProfileRepository = providerProfileRepository;
    }
    @Override
    public Optional<ProviderProfile> handle(CreateProviderProfileCommand command) {
        // Elimina el doble save y crea directamente el perfil
        var salonProfile = new ProviderProfile(
                command
        );
        return Optional.of(providerProfileRepository.save(salonProfile));
    }

    @Override
    public void handle(DeleteProviderProfileCommand command) {
        if(!providerProfileRepository.existsById(command.id())){
            throw new IllegalArgumentException("Service with this id does not exist");
        }
        try {
            providerProfileRepository.deleteById(command.id());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting service", e);
        }
    }

    @Override
    public Optional<ProviderProfile> handle(UpdateProviderProfileCommand command) {
        if(!providerProfileRepository.existsById(command.id())){
            throw new IllegalArgumentException("Salon Profile with this id does not exist");
        }
        var result = providerProfileRepository.findById(command.id());
        if (result.isEmpty()){
            throw new IllegalArgumentException("Salon Profile with this id does not exist");
        }
        var serviceToUpdate = result.get();
        try {
            var updatedService = providerProfileRepository.save(serviceToUpdate.updateInformation(command.profileUrl(), command.coverUrl()));
            return Optional.of(updatedService);
        } catch (Exception e){
            throw new IllegalArgumentException("Error while updating service", e);
        }
    }
}
