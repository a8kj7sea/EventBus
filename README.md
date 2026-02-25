# Flux | <sup><sub>[![](https://jitpack.io/v/a8kj7sea/Flux.svg)](https://jitpack.io/#a8kj7sea/Flux)</sup></sub>

Flux is a high-performance, concurrent event bus designed for Java 21+ that eliminates reflection overhead by utilizing ByteBuddy for runtime bytecode generation.

---

## Key Features

* **Bytecode Invocation**: Flux generates raw bytecode at runtime, providing near-native event dispatching speeds.
* **Fluent API**: The system utilizes a clean builder pattern for engine configuration and interceptor injection.
* **Concurrency**: It provides native support for Java 21 virtual threads and various asynchronous execution modes.
* **Smart Dispatching**: The engine supports priority-based handling, event cancellation, and filtering.
* **Modular Design**: The project is structured with decoupled modules for the API, invocation logic, and internal templates.
* **Shadow JAR**: The build process automatically relocates dependencies, such as ByteBuddy, into unique internal packages to prevent classpath conflicts with other libraries in the target environment.

---

## Quick Start

### 1. Define a Cancellable Event

```java
public class PlayerJoinEvent implements CancellableEvent {
    private final String playerName;
    private boolean cancelled = false;

    public PlayerJoinEvent(String name) { this.playerName = name; }
    public String getPlayerName() { return playerName; }

    @Override public boolean isCancelled() { return cancelled; }
    @Override public void setCancelled(boolean cancel) { this.cancelled = cancel; }
}

```

### 2. Create a Priority-Based Listener

```java
public class GameListener {
    @Subscribe(mode = Execution.SYNC, priority = 10)
    public void onBanCheck(PlayerJoinEvent event) {
        if ("BannedUser".equals(event.getPlayerName())) {
            event.setCancelled(true);
        }
    }

    @Subscribe(mode = Execution.VIRTUAL, ignoreCancelled = true)
    public void onAuditLog(PlayerJoinEvent event) {
        // Executes within a Java 21 Virtual Thread
        System.out.println("Audit: " + event.getPlayerName());
    }
}

```

### 3. Initialize and Publish

```java
FluxEngine engine = FluxBuilder.create()
    .invokerProvider(new BBInvokerProvider())
    .build();

engine.register(new GameListener());

// Publishing returns a Promise for tracking asynchronous completion
engine.publish(new PlayerJoinEvent("Steve"))
      .onSuccess(v -> System.out.println("Dispatch complete"));

```

---

## Module Overview

* **api**: Contains core interfaces for Events, Interceptors, and the Engine.
* **annotation**: Defines subscriber metadata like @Subscribe and Execution strategies.
* **invoker**: Contains the ByteBuddy implementation for reflection-free dispatching.
* **internal**: Manages standard orchestration and subscription registries.
* **template**: Provides high-performance concurrency utilities and Promises.

---

## Requirements

* **JDK**: 21 or higher.
* **Maven**: 3.8+ support.
