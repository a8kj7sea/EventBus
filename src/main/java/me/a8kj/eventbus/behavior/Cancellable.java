package me.a8kj.eventbus.behavior;

/**
 * Represents an event or action that can be cancelled.
 * <p>
 * Implementing this interface allows events or actions to be cancelled,
 * preventing further execution
 * based on specific conditions or logic.
 * </p>
 * 
 * @author a8kj7sea
 * @since 1.0
 */
public interface Cancellable {

    /**
     * Checks if the event or action is cancelled.
     * 
     * @return {@code true} if the event or action is cancelled, {@code false}
     *         otherwise.
     */
    boolean isCancelled();

    /**
     * Sets the cancellation state of the event or action.
     * <p>
     * When an event or action is cancelled, it typically prevents any further
     * handling or propagation.
     * </p>
     * 
     * @param cancel {@code true} to cancel the event or action, {@code false} to
     *               allow it to proceed.
     */
    void setCancelled(boolean cancel);
}
