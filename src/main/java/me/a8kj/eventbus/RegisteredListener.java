package me.a8kj.eventbus;

import java.lang.reflect.Method;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Represents a registered listener for a specific event method.
 * <p>
 * This class stores information about a listener object and the method that
 * should be invoked when the corresponding
 * event is triggered. It is used by the event bus system to map events to their
 * respective listener methods.
 * </p>
 * 
 * @author a8kj7sea
 * @since 1.0
 */
@RequiredArgsConstructor
@Getter
public class RegisteredListener {

    /**
     * The listener object that contains the method to handle the event.
     * This object is typically an instance of a class that implements the
     * {@link me.a8kj.eventbus.Listener} interface.
     */
    private final Object listener;

    /**
     * The method that should be invoked when the event is triggered.
     * This method must accept a single parameter of type
     * {@link me.a8kj.eventbus.Event}.
     */
    private final Method method;
}
