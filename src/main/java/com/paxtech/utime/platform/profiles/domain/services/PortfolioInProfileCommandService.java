package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreatePortfolioInProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.PortfolioInProfile;

import java.util.Optional;

public interface PortfolioInProfileCommandService {
    Optional<PortfolioInProfile> handle(CreatePortfolioInProfileCommand command);

}
