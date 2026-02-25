package me.a8kj.flux.template.timed;

import java.time.Instant;


public interface Traceable {

    Instant getOrigin();

    void setOrigin(Instant instant);

    Instant getLatest();

    void setLatest(Instant instant);


    default void trace() {
        Instant now = Instant.now();
        if (getOrigin() == null) {
            setOrigin(now);
        }
        setLatest(now);
    }
}