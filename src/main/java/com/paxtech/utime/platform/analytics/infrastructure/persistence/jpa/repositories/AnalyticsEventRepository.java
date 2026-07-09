package com.paxtech.utime.platform.analytics.infrastructure.persistence.jpa.repositories;

import com.paxtech.utime.platform.analytics.domain.model.aggregates.AnalyticsEvent;
import com.paxtech.utime.platform.analytics.domain.model.queries.EventTypeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {

    List<AnalyticsEvent> findAllByOrderByOccurredAtDesc();

    /**
     * Total number of recorded events grouped by event type, newest count first.
     */
    @Query("SELECT new com.paxtech.utime.platform.analytics.domain.model.queries.EventTypeCount(" +
            "e.eventType, COUNT(e)) " +
            "FROM AnalyticsEvent e GROUP BY e.eventType ORDER BY COUNT(e) DESC")
    List<EventTypeCount> countGroupedByEventType();
}
