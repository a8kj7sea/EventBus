package me.a8kj.flux.api;


import me.a8kj.flux.template.concurrent.Promise;

public interface ExecutionStrategy {
    Promise<Void> execute(Subscription subscription, Event event);
}
