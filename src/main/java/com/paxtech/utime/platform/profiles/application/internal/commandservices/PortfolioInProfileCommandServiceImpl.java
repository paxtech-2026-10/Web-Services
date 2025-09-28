package com.paxtech.utime.platform.profiles.application.internal.commandservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.PortfolioInProfile;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreatePortfolioInProfileCommand;
import com.paxtech.utime.platform.profiles.domain.services.PortfolioInProfileCommandService;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.PortfolioInProfileRepository;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.PortfolioImageRepository;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.ProviderProfileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PortfolioInProfileCommandServiceImpl implements PortfolioInProfileCommandService {

    private final PortfolioInProfileRepository relationRepository;
    private final PortfolioImageRepository portfolioImageRepository;
    private final ProviderProfileRepository providerProfileRepository;

    public PortfolioInProfileCommandServiceImpl(
            PortfolioInProfileRepository relationRepository,
            PortfolioImageRepository portfolioImageRepository,
            ProviderProfileRepository providerProfileRepository) {
        this.relationRepository = relationRepository;
        this.portfolioImageRepository = portfolioImageRepository;
        this.providerProfileRepository = providerProfileRepository;
    }

    @Override
    public Optional<PortfolioInProfile> handle(CreatePortfolioInProfileCommand command) {
        var portfolio = portfolioImageRepository.findById(command.portfolioImageId());
        var profile = providerProfileRepository.findById(command.providerProfileId());

        if (portfolio.isEmpty() || profile.isEmpty()) return Optional.empty();

        var relation = new PortfolioInProfile(portfolio.get(),profile.get());
        return Optional.of(relationRepository.save(relation));
    }
}
