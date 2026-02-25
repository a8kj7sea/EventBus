package me.a8kj.flux.internal.execution.strategy;

import me.a8kj.flux.api.Event;
import me.a8kj.flux.api.Subscription;
import me.a8kj.flux.api.ExecutionStrategy;
import me.a8kj.flux.template.concurrent.Promise;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncStrategy implements ExecutionStrategy {
    private static final ExecutorService POOL = Executors.newCachedThreadPool();

    @Override
    public Promise<Void> execute(Subscription subscription, Event event) {
        return Promise.supply(() -> {
            try {
                subscription.invoker().invoke(subscription.listener(), event);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
            return null;
        }, POOL);
    }
}
