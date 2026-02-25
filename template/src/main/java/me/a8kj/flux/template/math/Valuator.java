package me.a8kj.flux.template.math;


import me.a8kj.flux.template.storage.Repository;
import org.jetbrains.annotations.NotNull;

import java.util.function.BinaryOperator;

/**
 * A generic repository for managing and comparing numerical or measurable values.
 *
 * @param <K> The key type
 * @param <V> The value type (must be comparable)
 */
public interface Valuator<K, V extends Comparable<V>> extends Repository<K, V> {

    /**
     * Gets the current value or a provided fallback.
     */
    default V read(@NotNull K key, @NotNull V fallback) {
        return getOrDefault(key, fallback);
    }

    /**
     * Updates the value using a transformation function.
     * Use this to handle addition/subtraction for generic types.
     */
    default void compute(@NotNull K key, @NotNull V identity, @NotNull BinaryOperator<V> accumulator) {
        V current = read(key, identity);
        set(key, accumulator.apply(current, identity));
    }

    /**
     * Ensures the value does not drop below a certain threshold.
     */
    default void setWithFloor(@NotNull K key, @NotNull V value, @NotNull V floor) {
        if (value.compareTo(floor) < 0) {
            set(key, floor);
        } else {
            set(key, value);
        }
    }

    /**
     * Checks if the value for the given key is greater than the provided comparison.
     */
    default boolean isGreater(@NotNull K key, @NotNull V comparison) {
        return get(key)
                .map(val -> val.compareTo(comparison) > 0)
                .orElse(false);
    }
}