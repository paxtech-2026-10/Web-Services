package com.paxtech.utime.platform.analytics.domain.model.valueobjects;

/**
 * Identifies who triggered an analytics event.
 * ANONYMOUS is used when the action is fired by a visitor that is not
 * authenticated or whose identity is not relevant for the experiment.
 */
public enum ActorType {
    CLIENT,
    PROVIDER,
    ANONYMOUS;

    /**
     * Lenient parser: unknown or null values fall back to {@link #ANONYMOUS}
     * so a tracking call never fails because of a bad actor type.
     *
     * @param value the raw actor type coming from the client
     * @return the matching {@link ActorType} or {@link #ANONYMOUS}
     */
    public static ActorType fromString(String value) {
        if (value == null || value.isBlank()) return ANONYMOUS;
        try {
            return ActorType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ANONYMOUS;
        }
    }
}
