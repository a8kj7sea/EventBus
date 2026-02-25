package me.a8kj.flux.template.math;


import me.a8kj.flux.template.storage.Repository;
import org.jetbrains.annotations.NotNull;

/**
 * A specialized repository for tracking comparable metrics.
 *
 * @param <K> The key type
 * @param <V> The value type (e.g., Integer, Long, Double)
 */
public interface Metric<K, V extends Number & Comparable<V>> extends Repository<K, V> {

    /**
     * Returns the zero value for the specific number type.
     * Implementation must provide this (e.g., 0, 0L, 0.0).
     */
    @NotNull V zero();

    /**
     * Gets the current value or the zero constant if missing.
     */
    default @NotNull V current(@NotNull K key) {
        return getOrDefault(key, zero());
    }

    /**
     * Resets the value to the zero constant.
     */
    default void reset(@NotNull K key) {
        set(key, zero());
    }

    // --- Integer Specific Logic (Bridges) ---

    default void add(@NotNull K key, int amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount must be positive");
        // Cast logic or use doubleValue for generic arithmetic
        long newValue = current(key).longValue() + amount;
        set(key, cast(newValue));
    }

    default void subtract(@NotNull K key, int amount) {
        if (amount < 0) throw new IllegalArgumentException("Amount must be positive");
        long result = current(key).longValue() - amount;
        set(key, cast(Math.max(0, result)));
    }

    default void increment(@NotNull K key) {
        add(key, 1);
    }

    default void decrement(@NotNull K key) {
        subtract(key, 1);
    }

    /**
     * Internal helper to cast long back to the generic type V.
     */
    @SuppressWarnings("unchecked")
    private V cast(long value) {
        V z = zero();
        if (z instanceof Integer) return (V) Integer.valueOf((int) value);
        if (z instanceof Long) return (V) Long.valueOf(value);
        throw new UnsupportedOperationException("Unsupported number type for default arithmetic");
    }
}
