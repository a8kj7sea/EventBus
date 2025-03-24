package me.a8kj.eventbus;

/**
 * Interface representing a listener in the event bus system.
 * 
 * <h3>This is a marker interface to signify that a class is a listener for
 * events.</h3>
 * 
 * <p>
 * Implementing this interface marks a class as a listener for events. Any class
 * that implements this interface
 * can register to receive events. The specific methods that handle events will
 * typically be annotated with
 * {@link me.a8kj.eventbus.annotation.EventSubscribe}.
 * </p>
 * 
 * @author a8kj7sea
 * @since 1.0
 */
public interface Listener {

}
