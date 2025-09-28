package com.paxtech.utime.platform.profiles.application.internal.queryservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.PortfolioInProfile;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllPortfolioInProfilesByProviderProfileIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetPortfolioInProfileByIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetPortfolioInProfilesByProviderProfileIdQuery;
import com.paxtech.utime.platform.profiles.domain.services.PortfolioInProfileQueryService;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.PortfolioInProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PortfolioInProfileQueryServiceImpl implements PortfolioInProfileQueryService {

    private final PortfolioInProfileRepository repository;

    public PortfolioInProfileQueryServiceImpl(PortfolioInProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<PortfolioInProfile> handle(GetPortfolioInProfileByIdQuery query) {
        return repository.findById(query.id());
    }

    @Override
    public List<PortfolioInProfile> handle(GetAllPortfolioInProfilesByProviderProfileIdQuery query) {
        return repository.findAllByProviderProfileId(query.providerProfileId());
    }

    @Override
    public List<PortfolioInProfile> handle(GetPortfolioInProfilesByProviderProfileIdQuery query) {
        return repository.findAllByProviderProfileId(query.providerProfileId());
    }
}
