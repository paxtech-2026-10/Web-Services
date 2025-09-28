package com.paxtech.utime.platform.reservations.domain.model.aggregates;

import com.paxtech.utime.platform.reservations.domain.model.commands.CreateReservationCommand;
import com.paxtech.utime.platform.services.domain.model.aggregates.Service;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Reservation extends AuditableAbstractAggregateRoot<Reservation> {


    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long providerId;

    @Column(nullable = false)
    private Long paymentId;

    @Column(nullable = false, length = 32)
    private Long timeSlotId;

    @Column(nullable = false)
    private Long workerId;

    public Reservation() {}

    public Reservation(CreateReservationCommand command){
        this.clientId= command.clientId();
        this.providerId= command.providerId();
        this.paymentId= command.paymentId();
        this.timeSlotId = command.timeSlotId();
        this.workerId= command.workerId();
    }

    public Long getClientId() { return clientId; }
    public Long getProviderId() { return providerId; }
    public Long getPaymentId() { return paymentId; }
    public Long getTimeSlotId() { return timeSlotId; }
    public Long getWorkerId() { return workerId; }
}
