package com.paxtech.utime.platform.profiles.domain.model.aggregates;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateSocialInProfileCommand;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class SocialInProfile extends AuditableAbstractAggregateRoot<SocialInProfile> {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "provider_profile_id", nullable = false)
    private ProviderProfile providerProfile;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "social_id", nullable = false)
    private Social social;

    protected SocialInProfile() {} // JPA

    public SocialInProfile(CreateSocialInProfileCommand command, ProviderProfile profile, Social social) {
        this.providerProfile = profile;
        this.social = social;
    }

    private void validate(Long socialId, Long salonProfileId) {
        if (socialId == null || socialId <= 0)
            throw new IllegalArgumentException("Invalid socialId");
        if (salonProfileId == null || salonProfileId <= 0)
            throw new IllegalArgumentException("Invalid salonProfileId");
    }}
