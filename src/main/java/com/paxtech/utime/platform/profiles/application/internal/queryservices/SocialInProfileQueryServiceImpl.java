package com.paxtech.utime.platform.profiles.application.internal.queryservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.SocialInProfile;
import com.paxtech.utime.platform.profiles.domain.model.queries.*;
import com.paxtech.utime.platform.profiles.domain.services.SocialInProfileQueryService;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.SocialInProfileRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocialInProfileQueryServiceImpl implements SocialInProfileQueryService {

    private final SocialInProfileRepository repository;

    public SocialInProfileQueryServiceImpl(SocialInProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<SocialInProfile> handle(GetAllSocialsInProfilesQuery query) {
        return repository.findAll();
    }

    @Override
    public Optional<SocialInProfile> handle(GetSocialInProfileByIdQuery query) {
        return repository.findById(query.socialsInProfilesId());
    }

    @Override
    public List<SocialInProfile> handle(GetSocialsInProfileByProviderProfileIdQuery query) {
        return repository.findAllByProviderProfileId(query.providerProfileId());
    }

    @Override
    public List<SocialInProfile> handle(GetAllSocialsInProfileByProviderProfileIdQuery query) {
        return repository.findAllByProviderProfileId(query.providerProfileId());
    }
}
