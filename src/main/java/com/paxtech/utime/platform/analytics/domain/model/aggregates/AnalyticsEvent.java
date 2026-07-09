package com.paxtech.utime.platform.analytics.domain.model.aggregates;

import com.paxtech.utime.platform.analytics.domain.model.commands.RecordAnalyticsEventCommand;
import com.paxtech.utime.platform.analytics.domain.model.valueobjects.ActorType;
import com.paxtech.utime.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * A single analytics/tracking event captured for the experiment (UE01-UE05).
 * <p>
 * The aggregate is intentionally generic so it can host every tracked event
 * (calendar_viewed, reminder_sent, reservation_confirmed, payment_started,
 * payment_completed, book_in_app_started/completed, contact_whatsapp_initiated,
 * profile_view, ...). Event-specific data lives in the flexible {@link #metadata}
 * map so new events can be recorded without schema changes, while the actor
 * fields keep track of which client/provider triggered the action.
 */
@Entity
@Getter
public class AnalyticsEvent extends AuditableAbstractAggregateRoot<AnalyticsEvent> {

    @Column(nullable = false, length = 100)
    private String eventType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActorType actorType;

    /** Id of the client/provider that triggered the event (nullable for anonymous). */
    @Column
    private Long actorId;

    /** Provider/salon the action relates to (nullable). */
    @Column
    private Long providerId;

    /** Reservation the action relates to (nullable). */
    @Column
    private Long reservationId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "analytics_event_metadata", joinColumns = @JoinColumn(name = "event_id"))
    @MapKeyColumn(name = "meta_key", length = 100)
    @Column(name = "meta_value", length = 1000)
    private Map<String, String> metadata = new HashMap<>();

    @Column(nullable = false)
    private LocalDateTime occurredAt;

    public AnalyticsEvent() {}

    public AnalyticsEvent(RecordAnalyticsEventCommand command) {
        this.eventType = command.eventType();
        this.actorType = command.actorType() != null ? command.actorType() : ActorType.ANONYMOUS;
        this.actorId = command.actorId();
        this.providerId = command.providerId();
        this.reservationId = command.reservationId();
        this.metadata = command.metadata() != null ? new HashMap<>(command.metadata()) : new HashMap<>();
        this.occurredAt = LocalDateTime.now();
    }
}
