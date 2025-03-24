## TrashEventBus

This repository provides a custom event bus implementation for Java, allowing you to create and handle events with support for cancellation. It offers an easy-to-use API to register listeners, dispatch events, and manage event propagation. This system is designed to be lightweight and flexible, making it easy to integrate into various types of applications that require event-driven behavior.

# Example of Using the Custom Event Bus

This example demonstrates how to use the custom event bus system in your project. It includes basic functionality for triggering custom events, handling those events with listeners, and allowing event cancellation.

## Requirements

Before running this example, ensure that your project is set up with the necessary classes for event handling:

1. **`EventManager`** - The core class for managing event subscriptions and dispatching events.
2. **`Event`** - The base class for custom events.
3. **`Listener`** - An interface that all event listeners must implement.
4. **`Cancellable`** - Interface that custom events can implement to support cancellation.

## How It Works

### 1. **Event Registration**:
Listeners need to be registered to handle specific events. The `EventManager` class manages event listeners and is responsible for dispatching events to the registered listeners.

### 2. **Custom Event**:
You create custom events by extending the `Event` class. These events can be handled by registered listeners. For events that support cancellation, you can implement the `Cancellable` interface.

### 3. **Triggering Events**:
To trigger an event, you create an instance of the event and call `eventManager.callEvent(event)` to dispatch it.

### 4. **Event Cancellation**:
You can cancel events by calling `setCancelled(true)` on cancellable events. If an event is cancelled, its handler can prevent further actions.

---

## Example Usage

### 1. **Creating a Custom Event:**
Create a custom event class that extends `Event` and optionally implements `Cancellable`.

```java
public class GreetingEvent extends Event {
    private final String name;

    public GreetingEvent(String name) {
        super(false);  // False means it's not asynchronous by default
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
```

For a cancellable event:

```java
public class CancellableGreetingEvent extends GreetingEvent implements Cancellable {
    private boolean cancelled;

    public CancellableGreetingEvent(String name) {
        super(name);
        this.cancelled = false;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
```

### 2. **Creating a Listener:**
Create a listener class that will handle events. The listener class must implement the `Listener` interface and use `@EventSubscribe` annotations to subscribe to events.

```java
import me.a8kj.eventbus.annotation.EventSubscribe;

public class GreetingListener implements Listener {

    @EventSubscribe
    public void onGreeting(GreetingEvent event) {
        System.out.println("Received greeting for: " + event.getName());
    }

    @EventSubscribe
    public void onCancellableGreeting(CancellableGreetingEvent event) {
        if ("cancel".equals(event.getName())) {
            event.setCancelled(true);  // Cancel the event if the name is "cancel"
        }
    }
}
```

### 3. **Triggering Events:**
In your `main` method, you can trigger events using the `EventManager`:

```java
import me.a8kj.eventbus.manager.EventManager;

import java.util.Scanner;

public class Main {

    private static EventManager eventManager;

    public static void main(String[] args) {

        // Initialize the EventManager
        eventManager = new EventManager();
        eventManager.register(new GreetingListener());  // Register the listener

        // Read user input
        Scanner scanner = new Scanner(System.in);
        var name = scanner.nextLine();
        if (name != null) {
            // Trigger the GreetingEvent
            eventManager.callEvent(new GreetingEvent(name));

            // Trigger a cancellable greeting event
            triggerCancellableGreetingEvent(name);
        }
    }

    private static void triggerCancellableGreetingEvent(String name) {
        CancellableGreetingEvent event = new CancellableGreetingEvent(name);

        // Call the event
        eventManager.callEvent(event);

        // Check if the event was cancelled
        if (event.isCancelled()) {
            System.out.println("No greetings for you :) !");
            return;
        }

        System.out.println("Hi " + name);
    }
}
```

### 4. **How to Use**:
- **Run the program**: When you run the program, it will prompt for user input. Based on the input, it will trigger events.
- **Event Handling**: If you enter `"cancel"`, the cancellable event will be cancelled, and no greeting will be displayed.
- **Custom Event**: For other names, the greeting will be displayed.

---

## Key Concepts

### EventManager
- **`register(Object listener)`**: Registers an event listener.
- **`callEvent(Event event)`**: Dispatches the event to all registered listeners.
- **`unregister(Object listener)`**: Unregisters an event listener.

### Event
- The base class for custom events. You can create your own event classes by extending `Event`.
- **`getEventName()`**: Returns the name of the event (by default, the class name).

### Cancellable
- Implemented by events that support cancellation.
- **`isCancelled()`**: Returns whether the event is cancelled.
- **`setCancelled(boolean cancel)`**: Cancels the event.

### Listener
- An interface that all event listeners must implement. Methods in listeners are annotated with `@EventSubscribe` to indicate which events they should handle.

---

## Conclusion

This event bus system provides a flexible and easy way to handle custom events, trigger actions, and manage event cancellation in your application. It allows for highly modular code where events can be handled by different components of your system without tight coupling.
