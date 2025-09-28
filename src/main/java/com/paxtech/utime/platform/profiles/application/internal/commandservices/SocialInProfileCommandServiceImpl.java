// SocialInProfileCommandServiceImpl.java
package com.paxtech.utime.platform.profiles.application.internal.commandservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.ProviderProfile;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.Social;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.SocialInProfile;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateSocialInProfileCommand;
import com.paxtech.utime.platform.profiles.domain.services.SocialInProfileCommandService;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.ProviderProfileRepository;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.SocialInProfileRepository;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.SocialRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SocialInProfileCommandServiceImpl implements SocialInProfileCommandService {

    private final SocialInProfileRepository repository;
    private final ProviderProfileRepository profileRepository;
    private final SocialRepository socialRepository;

    public SocialInProfileCommandServiceImpl(SocialInProfileRepository repository,
                                             ProviderProfileRepository profileRepository,
                                             SocialRepository socialRepository) {
        this.repository = repository;
        this.profileRepository = profileRepository;
        this.socialRepository = socialRepository;
    }

    @Override
    public Optional<SocialInProfile> handle(CreateSocialInProfileCommand command) {
        Optional<ProviderProfile> profile = profileRepository.findById(command.providerProfileId());
        Optional<Social> social = socialRepository.findById(command.socialId());

        if (profile.isEmpty() || social.isEmpty()) return Optional.empty();

        var relation = new SocialInProfile(command, profile.get(), social.get());
        return Optional.of(repository.save(relation));
    }
}
