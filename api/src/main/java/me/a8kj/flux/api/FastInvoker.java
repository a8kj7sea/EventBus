package me.a8kj.flux.api;

@FunctionalInterface
public interface FastInvoker {
    void invoke(Object listener, Event event) throws Throwable;
}
