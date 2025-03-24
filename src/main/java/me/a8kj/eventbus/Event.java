package me.a8kj.eventbus;

import lombok.Getter;

/**
 * Represents an event in the event bus system.
 * <p>
 * This is an abstract class that serves as the base class for all events that
 * can be dispatched to listeners.
 * The event may be asynchronous, and it includes the event's name, which can be
 * used for identifying or categorizing events.
 * </p>
 * 
 * @author a8kj7sea
 * @since 1.0
 */
@Getter
public abstract class Event {

    /**
     * The name of the event. If not set, the event's name will default to the
     * simple class name.
     */
    private String name;

    /**
     * Indicates whether this event should be processed asynchronously.
     * If set to {@code true}, listeners will handle the event in an asynchronous
     * manner.
     */
    private final boolean asynchronous;

    /**
     * Default constructor that creates a synchronous event.
     */
    public Event() {
        this(false);
    }

    /**
     * Constructs an event with the specified asynchronous behavior.
     * 
     * @param isAsynchronous {@code true} if the event should be processed
     *                       asynchronously, {@code false} otherwise
     */
    public Event(boolean isAsynchronous) {
        this.asynchronous = isAsynchronous;
    }

    /**
     * Returns the name of the event. If the name has not been set explicitly, the
     * simple class name is used as the event name.
     * 
     * @return the name of the event
     */
    public String getEventName() {
        if (this.name == null) {
            this.name = this.getClass().getSimpleName();
        }

        return this.name;
    }

    /**
     * Enum representing the possible results of event handling.
     * <p>
     * The result indicates how event propagation should proceed:
     * </p>
     */
    public static enum Result {

        /**
         * Indicates that the event is denied and propagation should be stopped.
         * <p>
         * When an event handler returns this result, no further listeners will be
         * called, and the event will not continue
         * to other listeners or event handlers.
         * </p>
         */
        DENY,

        /**
         * Represents the default behavior, allowing the event to continue normally.
         * <p>
         * This is the standard behavior, where the event proceeds to the next listeners
         * in the event bus.
         * </p>
         */
        DEFAULT,

        /**
         * Indicates that the event continues, but with higher priority than the
         * default.
         * <p>
         * When this result is returned, the event continues to the next listeners, but
         * with priority.
         * This might be used to allow specific listeners to handle the event first,
         * depending on the bus system's logic.
         * </p>
         */
        ALLOW;
    }
}
