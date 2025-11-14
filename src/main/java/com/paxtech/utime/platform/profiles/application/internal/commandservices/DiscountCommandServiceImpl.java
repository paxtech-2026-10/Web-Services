package com.paxtech.utime.platform.profiles.application.internal.commandservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Discount;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateDiscountCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeleteDiscountCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateDiscountCommand;
import com.paxtech.utime.platform.profiles.domain.services.DiscountCommandService;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.DiscountRepository;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.ProviderProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DiscountCommandServiceImpl implements DiscountCommandService {

    private final DiscountRepository discountRepository;
    private final ProviderProfileRepository providerProfileRepository;

    public DiscountCommandServiceImpl(
            DiscountRepository discountRepository,
            ProviderProfileRepository providerProfileRepository) {
        this.discountRepository = discountRepository;
        this.providerProfileRepository = providerProfileRepository;
    }

    @Override
    public Optional<Discount> handle(CreateDiscountCommand command) {
        var providerProfileOpt = providerProfileRepository.findById(command.providerProfileId());
        if (providerProfileOpt.isEmpty()) {
            return Optional.empty();
        }

        var discount = new Discount(command, providerProfileOpt.get());
        return Optional.of(discountRepository.save(discount));
    }

    @Override
    public Optional<Discount> handle(UpdateDiscountCommand command) {
        var discountOpt = discountRepository.findById(command.id());
        if (discountOpt.isEmpty()) {
            return Optional.empty();
        }

        var discount = discountOpt.get();
        discount.update(command);
        return Optional.of(discountRepository.save(discount));
    }

    @Override
    public void handle(DeleteDiscountCommand command) {
        if (!discountRepository.existsById(command.id())) {
            throw new IllegalArgumentException("Discount with ID " + command.id() + " does not exist");
        }
        try {
            discountRepository.deleteById(command.id());
        } catch (Exception e) {
            throw new IllegalArgumentException("Error while deleting discount", e);
        }
    }
}

