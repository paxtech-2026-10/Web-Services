package com.paxtech.utime.platform.profiles.domain.services;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateSocialInProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateSocialsInProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeleteSocialsInProfileCommand;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreatePortfolioInProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdatePortfolioInProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.DeletePortfolioInProfileCommand;

import com.paxtech.utime.platform.profiles.domain.model.aggregates.SocialInProfile;
import com.paxtech.utime.platform.profiles.domain.model.aggregates.PortfolioInProfile;

import java.util.Optional;

public interface ProfileCommandService {

    // Commands for SocialsInProfile
    Optional<SocialInProfile> handle(CreateSocialInProfileCommand command);
    Optional<SocialInProfile> handle(UpdateSocialsInProfileCommand command);
    void handle(DeleteSocialsInProfileCommand command);

    // Commands for PortfolioInProfile
    Optional<PortfolioInProfile> handle(CreatePortfolioInProfileCommand command);
    Optional<PortfolioInProfile> handle(UpdatePortfolioInProfileCommand command);
    void handle(DeletePortfolioInProfileCommand command);
}
