package me.a8kj.flux.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class EventContext<E extends Event> {

    private final E event;
    private boolean propagationStopped = false;


    public void stopPropagation() {
        this.propagationStopped = true;
    }

}
