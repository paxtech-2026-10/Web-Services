package com.paxtech.utime.platform.profiles.application.internal.queryservices;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.PortfolioImage;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllPortfolioImagesQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetPortfolioImageByIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetPortfolioImageByUrlQuery;
import com.paxtech.utime.platform.profiles.domain.services.PortfolioImageQueryService;
import com.paxtech.utime.platform.profiles.infrastructure.persistence.jpa.repositories.PortfolioImageRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PortfolioImageQueryServiceImpl implements PortfolioImageQueryService {

    private final PortfolioImageRepository repository;

    public PortfolioImageQueryServiceImpl(PortfolioImageRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<PortfolioImage> handle(GetPortfolioImageByUrlQuery query) {
        return repository.findByImageUrl(query.imageUrl());
    }

    @Override
    public Optional<PortfolioImage> handle(GetPortfolioImageByIdQuery query) {
        return repository.findById(query.portfolioImageId());
    }

    @Override
    public List<PortfolioImage> handle(GetAllPortfolioImagesQuery query){
        return repository.findAll();
    }
}
