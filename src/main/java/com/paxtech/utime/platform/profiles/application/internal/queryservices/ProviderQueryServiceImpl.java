package com.paxtech.utime.platform.profiles.application.internal.queryservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllProvidersQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderByIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderByProfileIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderByUserIdQuery;
import com.paxtech.utime.platform.profiles.domain.services.ProviderQueryService;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of SalonsQueryService.
 */
@Service
public class ProviderQueryServiceImpl implements ProviderQueryService {

    private final ProviderRepository providerRepository;

    public ProviderQueryServiceImpl(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    @Override
    public List<Provider> handle(GetAllProvidersQuery query) {
        return providerRepository.findAll();
    }

    @Override
    public Optional<Provider> handle(GetProviderByIdQuery query) {
        return providerRepository.findById(query.id());
    }
    @Override
    public Optional<Provider> handle(GetProviderByProfileIdQuery query) {
        //return providerRepository.findByProviderProfile_Id(query.profileId());
        return null;
    }

    @Override
    public Optional<Provider> handle(GetProviderByUserIdQuery query){
        return providerRepository.findByUserId(query.userId());
    }

}
