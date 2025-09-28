package com.paxtech.utime.platform.reservations.domain.model.aggregates;

import com.paxtech.utime.platform.reservations.domain.model.commands.CreatePaymentCommand;
import com.paxtech.utime.platform.reservations.domain.model.valueobjects.Money;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

@Entity
public class Payment extends AuditableAbstractAggregateRoot<Payment> {

    @Getter
    @Embedded
    private Money money;

    @Getter
    @Column(nullable = false)
    private boolean status;

    public Payment() {}

    public Payment(CreatePaymentCommand command){
        this.money = new Money(command.amount(), command.currency());
        this.status = command.status();
    }
}
