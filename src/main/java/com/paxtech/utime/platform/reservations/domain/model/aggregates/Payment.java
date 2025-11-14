package com.paxtech.utime.platform.reservations.domain.model.aggregates;

import com.paxtech.utime.platform.reservations.domain.model.commands.CreatePaymentCommand;
import com.paxtech.utime.platform.reservations.domain.model.valueobjects.Money;
import com.paxtech.utime.platform.reservations.domain.model.valueobjects.PaymentStatus;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
public class Payment extends AuditableAbstractAggregateRoot<Payment> {

    @Getter
    @Embedded
    private Money money;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    // Campo temporal para compatibilidad con la columna 'status' existente en la BD
    @Column(name = "status", nullable = false)
    private boolean status;

    @Getter
    @Column(name = "stripe_payment_link_id", length = 255)
    private String stripePaymentLinkId;

    @Getter
    @Column(name = "stripe_checkout_session_id", length = 255)
    private String stripeCheckoutSessionId;

    @Getter
    @Column(name = "stripe_payment_intent_id", length = 255)
    private String stripePaymentIntentId;

    @Getter
    @Column(name = "reservation_id", nullable = false)
    private Long reservationId;

    @Getter
    @Column(name = "client_id", nullable = false)
    private Long clientId;

    public Payment() {}

    public Payment(CreatePaymentCommand command){
        this.money = new Money(command.amount(), command.currency());
        this.paymentStatus = PaymentStatus.PENDING;
        this.status = false; // PENDING = false
        this.reservationId = command.reservationId();
        this.clientId = command.clientId();
    }

    public void updateStripePaymentLinkId(String stripePaymentLinkId) {
        this.stripePaymentLinkId = stripePaymentLinkId;
    }

    public void updateStripeCheckoutSessionId(String stripeCheckoutSessionId) {
        this.stripeCheckoutSessionId = stripeCheckoutSessionId;
    }

    public void updateStripePaymentIntentId(String stripePaymentIntentId) {
        this.stripePaymentIntentId = stripePaymentIntentId;
    }

    public void markAsSucceeded() {
        this.paymentStatus = PaymentStatus.SUCCEEDED;
        this.status = true; // Sincronizar con el campo boolean
    }

    public void markAsFailed() {
        this.paymentStatus = PaymentStatus.FAILED;
        this.status = false; // Sincronizar con el campo boolean
    }

    public void markAsCanceled() {
        this.paymentStatus = PaymentStatus.CANCELED;
        this.status = false; // Sincronizar con el campo boolean
    }

    // Método de compatibilidad con el código existente
    @Deprecated
    public boolean getStatus() {
        return status;
    }

    // Método para sincronizar ambos campos cuando se carga desde la BD
    @PostLoad
    private void syncStatusFields() {
        if (paymentStatus != null) {
            // Si paymentStatus existe, sincronizar status desde paymentStatus
            this.status = paymentStatus == PaymentStatus.SUCCEEDED;
        } else {
            // Si paymentStatus es null, sincronizar paymentStatus desde status
            this.paymentStatus = status ? PaymentStatus.SUCCEEDED : PaymentStatus.PENDING;
        }
    }
}
