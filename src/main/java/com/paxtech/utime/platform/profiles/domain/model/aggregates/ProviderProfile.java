package com.paxtech.utime.platform.profiles.domain.model.aggregates;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateProviderProfileCommand;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
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

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;

    // Relaciones hacia otras entidades

    @OneToMany(mappedBy = "providerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SocialInProfile> socialLinks;

    @OneToMany(mappedBy = "providerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioInProfile> portfolioImages;

    @OneToMany(mappedBy = "providerProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Discount> discounts;

   /* @OneToOne(mappedBy = "providerProfile", fetch = FetchType.LAZY)
    private Provider provider;*/

    @Column(name = "provider_id", nullable = false, unique = true)
    private Long providerId;

    public ProviderProfile updateInformation(String profileUrl, String coverUrl, String location, String description, LocalTime openTime, LocalTime closeTime) {
        if (profileUrl != null){
            this.profileImageUrl = profileUrl;
        }
        if (coverUrl != null){
            this.coverImageUrl = coverUrl;
        }
        if (location != null){
            this.location = location;
        }
        if (description != null){
            this.description = description;
        }
        if (openTime != null){
            this.openTime = openTime;
        }
        if (closeTime != null){
            this.closeTime = closeTime;
        }
        return this;
    }

    public void updateDescription(String description) {
        this.description = description;
    }

    public void updateOpenTime(LocalTime openTime) {
        this.openTime = openTime;
    }

    public void updateCloseTime(LocalTime closeTime) {
        this.closeTime = closeTime;
    }

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void updateCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public ProviderProfile() {}

    public ProviderProfile(CreateProviderProfileCommand command) {
        this.profileImageUrl = command.profileUrl();
        this.coverImageUrl = command.coverUrl();
        this.location = command.location();
        this.providerId = command.providerId();
        this.description = command.description();
        this.openTime = command.openTime();
        this.closeTime = command.closeTime();
    }



}
