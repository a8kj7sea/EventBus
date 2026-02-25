package me.a8kj.flux.api;

public interface Interceptor {
    <E extends Event> boolean preDispatch(EventContext<E> context);
    <E extends Event> void postDispatch(EventContext<E> context);
}
