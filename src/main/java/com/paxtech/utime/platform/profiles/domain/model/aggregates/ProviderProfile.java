package com.paxtech.utime.platform.profiles.domain.model.aggregates;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateProviderProfileCommand;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class ProviderProfile extends AuditableAbstractAggregateRoot<ProviderProfile> {

    @Column(name = "profile_image_url", length = 150)
    private String profileImageUrl;

    @Column(name = "cover_image_url", length = 150)
    private String coverImageUrl;

    // Relaciones hacia otras entidades

    @OneToMany(mappedBy = "providerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SocialInProfile> socialLinks;

    @OneToMany(mappedBy = "providerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioInProfile> portfolioImages;

   /* @OneToOne(mappedBy = "providerProfile", fetch = FetchType.LAZY)
    private Provider provider;*/

    @Column(name = "provider_id", nullable = false, unique = true)
    private Long providerId;

    public ProviderProfile updateInformation(String profileUrl, String coverUrl) {
        this.profileImageUrl = profileUrl;
        this.coverImageUrl = coverUrl;
        return this;
    }

    public ProviderProfile() {}

    public ProviderProfile(CreateProviderProfileCommand command) {
        this.profileImageUrl = command.profileUrl();
        this.coverImageUrl = command.coverUrl();
        this.providerId = command.providerId();
    }



}
