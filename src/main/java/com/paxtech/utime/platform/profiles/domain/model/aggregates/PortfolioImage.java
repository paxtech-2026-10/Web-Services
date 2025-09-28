package com.paxtech.utime.platform.profiles.domain.model.aggregates;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreatePortfolioImageCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdatePortfolioImageCommand;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
@Getter
@Entity
public class PortfolioImage extends AuditableAbstractAggregateRoot<PortfolioImage> {

    @Getter
    @Column(nullable = false, length = 512)
    private String imageUrl;


    // Default constructor for JPA
    public PortfolioImage() {}

    public PortfolioImage(CreatePortfolioImageCommand command) {
        validate(command.imageUrl());
        this.imageUrl = command.imageUrl();
    }

    public PortfolioImage update(UpdatePortfolioImageCommand command) {
        validate(command.imageUrl());
        this.imageUrl = command.imageUrl();
        return this;
    }

    private void validate(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank() || imageUrl.length() > 200) {
            throw new IllegalArgumentException("Invalid portfolio image URL");
        }
    }
}
