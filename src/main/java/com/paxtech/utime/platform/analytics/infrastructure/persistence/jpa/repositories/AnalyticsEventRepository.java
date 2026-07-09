package com.paxtech.utime.platform.analytics.infrastructure.persistence.jpa.repositories;

import com.paxtech.utime.platform.analytics.domain.model.aggregates.AnalyticsEvent;
import com.paxtech.utime.platform.analytics.domain.model.queries.AnalyticsTimeRange;
import com.paxtech.utime.platform.analytics.domain.model.queries.EventTypeActorCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {

    List<AnalyticsEvent> findAllByOrderByOccurredAtDesc();

    /**
     * Number of events grouped by event type and actor type. The detailed
     * summary is composed in the query service from these rows.
     */
    @Query("SELECT new com.paxtech.utime.platform.analytics.domain.model.queries.EventTypeActorCount(" +
            "e.eventType, e.actorType, COUNT(e)) " +
            "FROM AnalyticsEvent e GROUP BY e.eventType, e.actorType")
    List<EventTypeActorCount> countByEventTypeAndActor();

    /**
     * Number of distinct (non-null) actors that have fired events.
     */
    @Query("SELECT COUNT(DISTINCT e.actorId) FROM AnalyticsEvent e WHERE e.actorId IS NOT NULL")
    long countDistinctActors();

    /**
     * Earliest and latest event timestamps (both null when there are no events).
     */
    @Query("SELECT new com.paxtech.utime.platform.analytics.domain.model.queries.AnalyticsTimeRange(" +
            "MIN(e.occurredAt), MAX(e.occurredAt)) FROM AnalyticsEvent e")
    AnalyticsTimeRange findTimeRange();
}
