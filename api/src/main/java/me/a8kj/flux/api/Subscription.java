package me.a8kj.flux.api;

import me.a8kj.flux.annotation.Execution;


public record Subscription(
        Object listener,
        FastInvoker invoker,
        Execution mode,
        int priority,
        boolean ignoreCancelled
) implements Comparable<Subscription> {

    @Override
    public int compareTo(Subscription other) {
        return Integer.compare(this.priority, other.priority);
    }
}
