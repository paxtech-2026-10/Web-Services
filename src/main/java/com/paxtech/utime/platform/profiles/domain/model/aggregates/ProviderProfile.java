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

    @Column(name = "location", length = 200)
    private String location;

    // Relaciones hacia otras entidades

    @OneToMany(mappedBy = "providerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SocialInProfile> socialLinks;

    @OneToMany(mappedBy = "providerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioInProfile> portfolioImages;

   /* @OneToOne(mappedBy = "providerProfile", fetch = FetchType.LAZY)
    private Provider provider;*/

    @Column(name = "provider_id", nullable = false, unique = true)
    private Long providerId;

    public ProviderProfile updateInformation(String profileUrl, String coverUrl, String location) {
        this.profileImageUrl = profileUrl;
        this.coverImageUrl = coverUrl;
        this.location = location;
        return this;
    }

    public ProviderProfile() {}

    public ProviderProfile(CreateProviderProfileCommand command) {
        this.profileImageUrl = command.profileUrl();
        this.coverImageUrl = command.coverUrl();
        this.location = command.location();
        this.providerId = command.providerId();
    }



}
