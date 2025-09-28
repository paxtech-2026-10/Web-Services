package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.PortfolioImage;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllClientsQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetAllPortfolioImagesQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetPortfolioImageByIdQuery;
import com.paxtech.utime.platform.profiles.domain.model.queries.GetPortfolioImageByUrlQuery;

import java.util.List;
import java.util.Optional;

public interface PortfolioImageQueryService {
    List<PortfolioImage> handle(GetAllPortfolioImagesQuery query);
    Optional<PortfolioImage> handle(GetPortfolioImageByIdQuery query);
    Optional<PortfolioImage> handle(GetPortfolioImageByUrlQuery query);
}
