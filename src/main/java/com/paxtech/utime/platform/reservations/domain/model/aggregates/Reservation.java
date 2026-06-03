package com.paxtech.utime.platform.reservations.domain.model.aggregates;

import com.paxtech.utime.platform.reservations.domain.model.commands.CreateReservationCommand;
import com.paxtech.utime.platform.reservations.domain.model.events.ReservationCreatedEvent;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;

@Entity
public class Reservation extends AuditableAbstractAggregateRoot<Reservation> {


    @Column(nullable = false)
    private Long clientId;

    @Column(nullable = false)
    private Long providerId;

    @Column(nullable = false)
    private Long serviceId;

    @Column(nullable = false)
    private Long timeSlotId;

    @Column(nullable = false)
    private Long workerId;

    public Reservation() {}

    public Reservation(CreateReservationCommand command){
        this.clientId= command.clientId();
        this.providerId= command.providerId();
        this.serviceId = command.serviceId();
        this.timeSlotId = command.timeSlotId();
        this.workerId= command.workerId();
        this.addDomainEvent(new ReservationCreatedEvent(this.timeSlotId));
    }

    public Long getClientId() { return clientId; }
    public Long getProviderId() { return providerId; }
    public Long getServiceId() { return serviceId; }
    public Long getTimeSlotId() { return timeSlotId; }
    public Long getWorkerId() { return workerId; }
}
