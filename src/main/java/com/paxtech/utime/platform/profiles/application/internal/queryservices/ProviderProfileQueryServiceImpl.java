package com.paxtech.utime.platform.profiles.application.internal.queryservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Provider;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.ProviderProfile;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllProviderProfilesQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderProfileByIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderProfileByProviderIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetProviderProfilesByCompanyNameQuery;
import com.paxtech.utime.platform.profiles.domain.services.ProviderProfileQueryService;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.ProviderProfileRepository;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.ProviderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProviderProfileQueryServiceImpl implements ProviderProfileQueryService {
    private final ProviderProfileRepository providerProfileRepository;
    private final ProviderRepository providerRepository;

    public ProviderProfileQueryServiceImpl(ProviderProfileRepository providerProfileRepository, ProviderRepository providerRepository) {
        this.providerProfileRepository = providerProfileRepository;
        this.providerRepository = providerRepository;
    }

    @Override
    public Optional<ProviderProfile> handle(GetProviderProfileByIdQuery query) {
        return providerProfileRepository.findById(query.id());
    }

    @Override
    public Optional<ProviderProfile> handle(GetProviderProfileByProviderIdQuery query) {
        return providerProfileRepository.findByProviderId(query.providerId());
    }

    @Override
    public List<ProviderProfile> handle(GetAllProviderProfilesQuery query){
        return providerProfileRepository.findAll();
    }

    @Override
    public List<ProviderProfile> handle(GetProviderProfilesByCompanyNameQuery query) {
        String companyName = query.companyName().value();
        List<Provider> providers = providerRepository.findByCompanyNameContainingIgnoreCase(companyName);

        if (providers.isEmpty()){
            return List.of();
        }
        //Extraer los IDs de los Providers encontrados
        List<Long> providerIds = providers.stream().map(Provider::getId).toList();
        return providerProfileRepository.findByProviderIdIn(providerIds);
    }
}
