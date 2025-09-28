package com.paxtech.utime.platform.profiles.application.internal.queryservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.Social;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllSocialsQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetSocialByIconQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetSocialByIdQuery;
import com.paxtech.utime.platform.profiles.domain.services.SocialQueryService;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.SocialRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocialQueryServiceImpl implements SocialQueryService {

    private final SocialRepository socialRepository;

    public SocialQueryServiceImpl(SocialRepository socialRepository) {
        this.socialRepository = socialRepository;
    }

    @Override
    public Optional<Social> handle(GetSocialByIconQuery query) {
        return socialRepository.findBySocialIcon(query.socialIcon());
    }

    @Override
    public Optional<Social> handle(GetSocialByIdQuery query) {
        return socialRepository.findById(query.socialId());
    }

    @Override
    public List<Social> handle(GetAllSocialsQuery query){
        return socialRepository.findAll();
    }
}
