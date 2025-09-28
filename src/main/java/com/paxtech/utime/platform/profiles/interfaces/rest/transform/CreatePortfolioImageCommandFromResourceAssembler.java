package com.paxtech.utime.platform.profiles.interfaces.rest.transform;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreatePortfolioImageCommand;
import com.paxtech.utime.platform.profiles.interfaces.rest.resources.CreatePortfolioImageResource;

public class CreatePortfolioImageCommandFromResourceAssembler {
    public static CreatePortfolioImageCommand toCommandFromResource(CreatePortfolioImageResource resource) {
        return new CreatePortfolioImageCommand(resource.imageUrl());
    }
}
