package com.paxtech.utime.platform.profiles.domain.model.aggregates;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreatePortfolioInProfileCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdatePortfolioInProfileCommand;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
@Getter
@Entity
public class PortfolioInProfile extends AuditableAbstractAggregateRoot<PortfolioInProfile> {

    @ManyToOne(optional = false)
    @JoinColumn(name = "portfolio_id", nullable = false)
    private PortfolioImage portfolio;

    @ManyToOne(optional = false)
    @JoinColumn(name = "provider_profile_id", nullable = false)
    private ProviderProfile providerProfile;

    protected PortfolioInProfile() {}

    public PortfolioInProfile(PortfolioImage portfolio, ProviderProfile providerProfile) {
        this.portfolio = portfolio;
        this.providerProfile = providerProfile;
    }
}
