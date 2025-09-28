package com.paxtech.utime.platform.profiles.domain.model.aggregates;

import com.paxtech.utime.platform.iam.domain.model.aggregates.User;
import com.paxtech.utime.platform.profiles.domain.model.commands.CreateClientCommand;
import com.paxtech.utime.platform.profiles.domain.model.valueobjects.*;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class Client extends AuditableAbstractAggregateRoot<Client> {

    @Embedded
    private FullName fullName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Client(CreateClientCommand command, User user) {
        this.fullName = new FullName(command.firstName(), command.lastName());
        this.user = user;
    }

    protected Client() {}


    public User getUser() { return user; }

    public String getFirstName() {
        return fullName.getFirstName();
    }

    public String getLastName() {
        return fullName.getLastName();
    }

    public String getFullName() {
        return fullName.getFullName();
    }

}
