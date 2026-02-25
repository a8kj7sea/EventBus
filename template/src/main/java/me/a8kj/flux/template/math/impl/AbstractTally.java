package me.a8kj.flux.template.math.impl;

import me.a8kj.flux.template.math.Tally;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A thread-safe base implementation of the Tally interface.
 */
public abstract class AbstractTally<K> implements Tally<K> {

    protected final Map<K, Long> storage = new ConcurrentHashMap<>();

    @Override
    public @NotNull Map<K, Long> handle() {
        return storage;
    }

    @Override
    public void set(@NotNull K key, @NotNull Long value) {
        storage.put(key, value);
    }

    @Override
    public void remove(@NotNull K key) {
        storage.remove(key);
    }
}