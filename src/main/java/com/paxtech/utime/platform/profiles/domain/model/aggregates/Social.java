package com.paxtech.utime.platform.profiles.domain.model.aggregates;

import com.paxtech.utime.platform.profiles.domain.model.commands.CreateSocialCommand;
import com.paxtech.utime.platform.profiles.domain.model.commands.UpdateSocialCommand;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Entity
public class Social extends AuditableAbstractAggregateRoot<Social> {

    @Getter
    @Column(name = "social_url", length = 100, nullable = false)
    private String socialUrl;

    @Getter
    @Column(name = "social_icon", length = 32, nullable = false)
    private String socialIcon;

    protected Social() {} // JPA constructor

    public Social(CreateSocialCommand command) {
        validate(command.socialUrl(), command.socialIcon());
        this.socialUrl = command.socialUrl();
        this.socialIcon = command.socialIcon();
    }

    public Social update(UpdateSocialCommand command) {
        validate(command.socialUrl(), command.socialIcon());
        this.socialUrl = command.socialUrl();
        this.socialIcon = command.socialIcon();
        return this;
    }

    private void validate(String socialUrl, String socialIcon) {
        if (socialUrl == null || socialUrl.isBlank() || socialUrl.length() > 100)
            throw new IllegalArgumentException("Invalid social URL");
        if (socialIcon == null || socialIcon.isBlank() || socialIcon.length() > 32)
            throw new IllegalArgumentException("Invalid social icon");
    }
}
