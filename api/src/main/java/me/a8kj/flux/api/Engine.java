package me.a8kj.flux.api;

import me.a8kj.flux.annotation.Execution;
import me.a8kj.flux.template.concurrent.Promise;

import java.util.function.Consumer;

public interface Engine {

    <E extends Event> Promise<Void> publish(E event);

    void register(Object listener);

    void unregister(Object listener);

    <E extends Event> void subscribe(Class<E> eventClass, Consumer<E> consumer);

    <E extends Event> void subscribe(Class<E> eventClass, Consumer<E> consumer, Execution mode);

    void addInterceptor(Interceptor interceptor);

    boolean isActive();

    void shutdown();
}