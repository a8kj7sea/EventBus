package me.a8kj.flux.internal.execution.strategy;


import me.a8kj.flux.api.Event;
import me.a8kj.flux.api.Subscription;
import me.a8kj.flux.api.ExecutionStrategy;
import me.a8kj.flux.template.concurrent.Promise;

import java.util.concurrent.CompletableFuture;

public class SyncStrategy implements ExecutionStrategy {
    @Override
    public Promise<Void> execute(Subscription subscription, Event event) {
        try {
            subscription.invoker().invoke(subscription.listener(), event);
            return Promise.completed(null);
        } catch (Throwable t) {
            CompletableFuture<Void> failedFuture = new CompletableFuture<>();
            failedFuture.completeExceptionally(t);
            return Promise.of(failedFuture);
        }
    }
}
