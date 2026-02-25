package me.a8kj.flux.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import me.a8kj.flux.annotation.Execution;
import me.a8kj.flux.annotation.Subscribe;
import me.a8kj.flux.api.*;
import me.a8kj.flux.api.EventContext;
import me.a8kj.flux.api.Event;
import me.a8kj.flux.api.ExecutionStrategy;
import me.a8kj.flux.internal.execution.strategy.AsyncStrategy;
import me.a8kj.flux.internal.execution.strategy.SyncStrategy;
import me.a8kj.flux.internal.execution.strategy.VirtualStrategy;
import me.a8kj.flux.invoker.InvokerProvider;
import me.a8kj.flux.template.Cancellable;
import me.a8kj.flux.template.concurrent.Promise;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

@Log
@RequiredArgsConstructor
public class DefaultEngine implements Engine {

    private final ConcurrentHashMap<Class<?>, CopyOnWriteArrayList<Subscription>> registry = new ConcurrentHashMap<>();
    private final List<Interceptor> interceptors = new CopyOnWriteArrayList<>();
    private final AtomicBoolean active = new AtomicBoolean(true);
    private final InvokerProvider invokerProvider;

    private final Map<Execution, ExecutionStrategy> strategies = Map.of(
            Execution.SYNC, new SyncStrategy(),
            Execution.ASYNC, new AsyncStrategy(),
            Execution.VIRTUAL, new VirtualStrategy()
    );

    @Override
    public void register(Object listener) {
        if (!checkActive() || listener == null) return;

        Class<?> clazz = listener.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            Subscribe annotation = method.getAnnotation(Subscribe.class);
            if (annotation == null) continue;

            validateMethod(method);

            Class<?> eventType = method.getParameterTypes()[0];
            FastInvoker invoker = invokerProvider.provide(method);

            Subscription sub = new Subscription(
                    listener,
                    invoker,
                    annotation.mode(),
                    annotation.priority(),
                    annotation.ignoreCancelled()
            );

            registry.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(sub);
            registry.get(eventType).sort(Subscription::compareTo);
        }
    }

    @Override
    public void unregister(Object listener) {
        if (listener == null) return;
        registry.values().forEach(subs -> subs.removeIf(sub -> sub.listener() == listener));
    }

    @Override
    public <E extends Event> void subscribe(Class<E> eventClass, Consumer<E> consumer, Execution mode) {
        if (!checkActive()) return;
        FastInvoker lambdaInvoker = (listener, event) -> consumer.accept((E) event);
        Subscription sub = new Subscription(null, lambdaInvoker, mode, 0, false);
        registry.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>()).add(sub);
    }

    @Override
    public <E extends Event> void subscribe(Class<E> eventClass, Consumer<E> consumer) {
        this.subscribe(eventClass, consumer, Execution.SYNC);
    }

    @Override
    public void addInterceptor(Interceptor interceptor) {
        if (interceptor != null) this.interceptors.add(interceptor);
    }

    @Override
    public boolean isActive() {
        return active.get();
    }

    @Override
    public <E extends Event> Promise<Void> publish(E event) {
        if (!checkActive()) return Promise.completed(null);

        long start = System.nanoTime();
        List<Subscription> subscriptions = registry.get(event.getClass());
        if (subscriptions == null || subscriptions.isEmpty()) return Promise.completed(null);

        EventContext<E> context = new EventContext<>(event);

        for (Interceptor interceptor : interceptors) {
            if (!interceptor.preDispatch(context)) return Promise.completed(null);
        }

        List<Promise<Void>> promises = new ArrayList<>();
        for (Subscription sub : subscriptions) {
            if (context.isPropagationStopped()) break;
            if (event instanceof Cancellable can && can.isCancelled() && !sub.ignoreCancelled()) continue;

            ExecutionStrategy strategy = strategies.get(sub.mode());
            if (strategy != null) promises.add(strategy.execute(sub, event));
        }

        CompletableFuture<Void>[] futures = promises.stream()
                .map(Promise::unwrap)
                .toArray(CompletableFuture[]::new);

        return Promise.of(CompletableFuture.allOf(futures))
                .onSuccess(v -> {
                    runPostDispatch(context);
                    long duration = System.nanoTime() - start;
                    log.info(String.format("[Flux-Profile] %s dispatched in %.4f ms",
                            event.getClass().getSimpleName(), duration / 1_000_000.0));
                });
    }

    private void runPostDispatch(EventContext<?> context) {
        for (Interceptor interceptor : interceptors) {
            try {
                interceptor.postDispatch(context);
            } catch (Exception e) {
                log.severe("Post-dispatch error: " + e.getMessage());
            }
        }
    }

    @Override
    public void shutdown() {
        if (!active.getAndSet(false)) return;
        registry.clear();
        interceptors.clear();
        invokerProvider.clear();
        strategies.values().forEach(s -> {
            if (s instanceof AutoCloseable c) {
                try {
                    c.close();
                } catch (Exception ignored) {
                }
            }
        });
    }

    private boolean checkActive() {
        return active.get();
    }

    private void validateMethod(Method method) {
        if (method.getParameterCount() != 1 || !Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
            throw new IllegalArgumentException("Invalid subscriber method: " + method.getName());
        }
    }
}