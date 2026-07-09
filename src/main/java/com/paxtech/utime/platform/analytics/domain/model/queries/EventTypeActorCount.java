package com.paxtech.utime.platform.analytics.domain.model.queries;

import com.paxtech.utime.platform.analytics.domain.model.valueobjects.ActorType;

/**
 * Raw aggregation row: number of events for a given (eventType, actorType) pair.
 * Used to build the detailed analytics summary. {@code total} is a boxed
 * {@link Long} so it matches the JPQL {@code COUNT(e)} in the constructor
 * expression.
 */
public record EventTypeActorCount(String eventType, ActorType actorType, Long total) {
}
