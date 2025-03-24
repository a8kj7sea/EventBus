package me.a8kj.eventbus.manager;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import me.a8kj.eventbus.Event;
import me.a8kj.eventbus.Listener;
import me.a8kj.eventbus.RegisteredListener;
import me.a8kj.eventbus.annotation.EventSubscribe;
import me.a8kj.eventbus.exception.RuntimeEventException;

/**
 * Manages the registration and unregistration of event listeners and
 * dispatching events to the appropriate listeners.
 * <p>
 * The {@code EventManager} keeps track of methods annotated with
 * {@code @EventSubscribe} and invokes them when an event
 * of the corresponding type is fired. It ensures that listeners are properly
 * registered and unregistered.
 * </p>
 * 
 * @author a8kj7sea
 * @since 1.0
 */
public class EventManager {

    /**
     * A map that associates an {@link Event} type with a list of registered
     * listeners for that event type.
     * <p>
     * This map stores listeners by event type and ensures each event type has its
     * corresponding listener methods.
     * </p>
     */
    private final Map<Class<? extends Event>, List<RegisteredListener>> listeners = new HashMap<>();

    /**
     * Registers an object as an event listener. The listener must implement the
     * {@link Listener} interface.
     * Methods in the listener class that are annotated with {@link EventSubscribe}
     * will be registered to listen for
     * events of a specific type.
     * 
     * @param listener the object to register as an event listener
     * @throws IllegalArgumentException if the listener does not implement the
     *                                  {@link Listener} interface
     * @throws IllegalArgumentException if any method annotated with
     *                                  {@link EventSubscribe} has a parameter type
     *                                  that is not a subclass of {@link Event}
     */
    @SuppressWarnings("unchecked")
    public synchronized void register(Object listener) {
        if (!(listener instanceof Listener)) {
            throw new IllegalArgumentException("Listener must implement EventListener interface.");
        }

        for (Method method : listener.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(EventSubscribe.class) && method.getParameterCount() == 1) {
                Class<?> parameterType = method.getParameterTypes()[0];
                if (!Event.class.isAssignableFrom(parameterType)) {
                    throw new IllegalArgumentException(
                            "EventSubscribe methods must have a single parameter of type Event.");
                }

                RegisteredListener registeredListener = new RegisteredListener(listener, method);

                synchronized (listeners) {
                    listeners.computeIfAbsent((Class<? extends Event>) parameterType, k -> new CopyOnWriteArrayList<>())
                            .add(registeredListener);
                }
            }
        }
    }

    /**
     * Unregisters an object from being an event listener. The listener must
     * implement the {@link Listener} interface.
     * 
     * @param listener the object to unregister from event listening
     * @throws IllegalArgumentException if the listener does not implement the
     *                                  {@link Listener} interface
     */
    public synchronized void unregister(Object listener) {
        if (!(listener instanceof Listener)) {
            throw new IllegalArgumentException("Listener must implement EventListener interface.");
        }

        synchronized (listeners) {
            listeners.values().forEach(
                    registeredListeners -> registeredListeners.removeIf(rl -> rl.getListener().equals(listener)));
        }
    }

    /**
     * Registers a list of objects as event listeners. Each object in the list is
     * registered individually using the
     * {@link #register(Object)} method.
     * 
     * @param listeners a list of objects to register as event listeners
     */
    public synchronized void registerAll(List<Object> listeners) {
        listeners.forEach(this::register);
    }

    /**
     * Unregisters a list of objects as event listeners. Each object in the list is
     * unregistered individually using the
     * {@link #unregister(Object)} method.
     * 
     * @param listeners a list of objects to unregister from event listening
     */
    public synchronized void unregisterAll(List<Object> listeners) {
        listeners.forEach(this::unregister);
    }

    /**
     * Dispatches an event to all registered listeners for that event type. Each
     * listener's method annotated with
     * {@link EventSubscribe} will be invoked with the event as the parameter.
     * 
     * @param event the event to dispatch
     * @throws RuntimeEventException if an error occurs while invoking the listener
     *                               method
     */
    public void callEvent(Event event) {
        List<RegisteredListener> registeredListeners;

        synchronized (listeners) {
            registeredListeners = listeners.get(event.getClass());
        }

        if (registeredListeners != null) {
            for (RegisteredListener registeredListener : registeredListeners) {
                try {
                    registeredListener.getMethod().invoke(registeredListener.getListener(), event);
                } catch (Exception e) {
                    throw new RuntimeEventException(e,
                            "Error while dispatching event: " + event.getClass().getSimpleName());
                }
            }
        }
    }
}
