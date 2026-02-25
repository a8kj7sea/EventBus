package me.a8kj.flux.template.storage;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * A generic contract for key-value based data management.
 */
public interface Repository<K, V> {

    /**
     * Internal access to the data. Should be used by implementations only.
     */
    @NotNull Map<K, V> handle();

    /**
     * Returns an immutable view of the underlying data.
     */
    default @NotNull @Unmodifiable Map<K, V> asMap() {
        return Collections.unmodifiableMap(handle());
    }

    void set(@NotNull K key, @NotNull V value);

    default Optional<V> get(@NotNull K key) {
        return Optional.ofNullable(handle().get(key));
    }

    default @NotNull V getOrDefault(@NotNull K key, @NotNull V defaultValue) {
        return handle().getOrDefault(key, defaultValue);
    }

    default void remove(@NotNull K key) {
        handle().remove(key);
    }

    default boolean contains(@NotNull K key) {
        return handle().containsKey(key);
    }
}