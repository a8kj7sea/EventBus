package me.a8kj.eventbus.manager;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import me.a8kj.eventbus.Event;
import me.a8kj.eventbus.Listener;
import me.a8kj.eventbus.RegisteredListener;
import me.a8kj.eventbus.annotation.EventSubscribe;
import me.a8kj.eventbus.exception.RuntimeEventException;

/**
 * Thread-safe manager responsible for registering event listeners
 * and dispatching events to them.
 * <p>
 * The {@code EventManager} scans listener objects for methods annotated
 * with {@link EventSubscribe} and registers them based on their event
 * parameter type.
 * </p>
 *
 * <p>
 * This implementation is fully thread-safe and optimized for
 * high-frequency event dispatching by using:
 * </p>
 * <ul>
 *   <li>{@link ConcurrentHashMap} for event-to-listener mapping</li>
 *   <li>{@link CopyOnWriteArrayList} for safe iteration during dispatch</li>
 * </ul>
 *
 * @author a8kj7sea
 * @since 1.1
 */
public class EventManager {

    /**
     * Stores registered listeners mapped by their event type.
     * <p>
     * Key: Event class<br>
     * Value: Thread-safe list of listeners registered for that event
     * </p>
     */
    private final Map<Class<? extends Event>, CopyOnWriteArrayList<RegisteredListener>> listeners =
            new ConcurrentHashMap<>();

    /**
     * Registers an object as an event listener.
     * <p>
     * The given object must implement {@link Listener}. All methods annotated
     * with {@link EventSubscribe} and having exactly one parameter extending
     * {@link Event} will be registered.
     * </p>
     *
     * @param listener the listener instance to register
     *
     * @throws IllegalArgumentException if the object does not implement {@link Listener}
     * @throws IllegalArgumentException if an annotated method has an invalid signature
     */
    @SuppressWarnings("unchecked")
    public void register(Object listener) {
        if (!(listener instanceof Listener)) {
            throw new IllegalArgumentException("Listener must implement Listener interface.");
        }

        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (!method.isAnnotationPresent(EventSubscribe.class)) {
                continue;
            }

            if (method.getParameterCount() != 1) {
                throw new IllegalArgumentException(
                        "EventSubscribe methods must have exactly one parameter.");
            }

            Class<?> parameterType = method.getParameterTypes()[0];
            if (!Event.class.isAssignableFrom(parameterType)) {
                throw new IllegalArgumentException(
                        "EventSubscribe parameter must extend Event.");
            }

            method.setAccessible(true);

            RegisteredListener registeredListener =
                    new RegisteredListener(listener, method);

            listeners
                    .computeIfAbsent(
                            (Class<? extends Event>) parameterType,
                            k -> new CopyOnWriteArrayList<>()
                    )
                    .add(registeredListener);
        }
    }

    /**
     * Unregisters all event handlers belonging to the given listener.
     *
     * @param listener the listener instance to unregister
     *
     * @throws IllegalArgumentException if the object does not implement {@link Listener}
     */
    public void unregister(Object listener) {
        if (!(listener instanceof Listener)) {
            throw new IllegalArgumentException("Listener must implement Listener interface.");
        }

        listeners.values().forEach(list ->
                list.removeIf(rl -> rl.getListener() == listener)
        );
    }

    /**
     * Registers multiple listeners at once.
     *
     * @param listeners a list of listener objects to register
     */
    public void registerAll(List<Object> listeners) {
        listeners.forEach(this::register);
    }

    /**
     * Unregisters multiple listeners at once.
     *
     * @param listeners a list of listener objects to unregister
     */
    public void unregisterAll(List<Object> listeners) {
        listeners.forEach(this::unregister);
    }

    /**
     * Dispatches an event to all registered listeners for its exact type.
     * <p>
     * Listener invocation is performed sequentially in the calling thread.
     * This method is lock-free and safe to call concurrently.
     * </p>
     *
     * @param event the event to dispatch
     *
     * @throws RuntimeEventException if a listener invocation fails
     */
    public void callEvent(Event event) {
        List<RegisteredListener> registeredListeners =
                listeners.get(event.getClass());

        if (registeredListeners == null || registeredListeners.isEmpty()) {
            return;
        }

        for (RegisteredListener registeredListener : registeredListeners) {
            try {
                registeredListener.getMethod()
                        .invoke(registeredListener.getListener(), event);
            } catch (Throwable t) {
                throw new RuntimeEventException(
                        t,
                        "Error while dispatching event: " +
                                event.getClass().getSimpleName()
                );
            }
        }
    }
}
