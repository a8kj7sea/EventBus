package me.a8kj.flux.template.timed;

import java.time.Instant;

/**
 * Defines a contract for objects that maintain a record of when they were created or modified.
 */
public interface Timestamped {

    /**
     * Gets the timestamp associated with this object.
     * @return the relevant {@link Instant}, or null if not yet set.
     */
    Instant getTimestamp();

    /**
     * Sets the timestamp for this object.
     * Default implementation allows for optional override if the object is mutable.
     * * @param timestamp the {@link Instant} to set
     */
    default void setTimestamp(Instant timestamp) {
        // Default to no-op or throw UnsupportedOperationException if immutability is preferred
    }
}