package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.PortfolioImage;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreatePortfolioImageCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeletePortfolioImageCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdatePortfolioImageCommand;

import java.util.Optional;

public interface PortfolioImageCommandService {
    Optional<PortfolioImage> handle(CreatePortfolioImageCommand command);
    Optional<PortfolioImage> handle(UpdatePortfolioImageCommand command);
    void handle(DeletePortfolioImageCommand command);

}
