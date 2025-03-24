package me.a8kj.example;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.a8kj.eventbus.Event;
import me.a8kj.eventbus.behavior.Cancellable;

@Getter
@RequiredArgsConstructor
public class CancellableGreetingEvent extends Event implements Cancellable {

    private boolean cancelled;
    private final String name;

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
}
