package com.paxtech.utime.platform.profiles.application.internal.commandservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Social;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateSocialCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeleteSocialCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateSocialCommand;
import com.paxtech.utime.platform.profiles.domain.services.SocialCommandService;
import com.paxtech.utime.platform.profiles.domain.services.SocialQueryService;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.SocialRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SocialCommandServiceImpl implements SocialCommandService {
    private final SocialRepository socialRepository;
    public SocialCommandServiceImpl(SocialRepository socialRepository) {
        this.socialRepository = socialRepository;
    }
    @Override
    public Optional<Social> handle(CreateSocialCommand command){
        var social = new Social(command);
        socialRepository.save(social);
        return Optional.of(social);
    }
    @Override
    public Optional<Social> handle(UpdateSocialCommand command) {
        return socialRepository.findById(command.id())
                .map(existing -> {
                    existing.update(command);
                    return socialRepository.save(existing);
                });
    }

    @Override
    public void handle(DeleteSocialCommand command) {
        socialRepository.deleteById(command.id());
    }
}
