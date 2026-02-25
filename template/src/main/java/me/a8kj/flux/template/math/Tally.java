package me.a8kj.flux.template.math;

import me.a8kj.flux.template.storage.Repository;
import org.jetbrains.annotations.NotNull;

/**
 * A specialized repository for managing and manipulating numerical counters.
 * * <p>This interface is designed for tracking metrics like PvP kills,
 * currency balances, or session activity counts.</p>
 *
 * @param <K> The key type used to categorize the counts.
 */
public interface Tally<K> extends Repository<K, Long> {

    /**
     * Gets the current count for a key.
     * * @param key The key to look up.
     *
     * @return The current value, or 0 if no record exists.
     */
    default long count(@NotNull K key) {
        return getOrDefault(key, 0L);
    }

    /**
     * Increases a value by a specific amount.
     * * @param key    The key to increase.
     *
     * @param amount The positive value to add.
     * @throws IllegalArgumentException if amount is negative.
     */
    default void add(@NotNull K key, long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Addition amount must be positive. Use subtract() for negative changes.");
        }
        set(key, count(key) + amount);
    }

    /**
     * Decreases a value by a specific amount, but ensures it never drops below a floor (usually zero).
     * * @param key    The key to decrease.
     *
     * @param amount The amount to remove.
     */
    default void subtract(@NotNull K key, long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Subtraction amount must be positive.");
        }
        long current = count(key);
        // Standard game logic: you cannot have negative kills or negative money.
        set(key, Math.max(0L, current - amount));
    }

    /**
     * Increments the count for the given key by 1.
     */
    default void increment(@NotNull K key) {
        add(key, 1L);
    }

    /**
     * Decrements the count for the given key by 1, with a floor of zero.
     */
    default void decrement(@NotNull K key) {
        subtract(key, 1L);
    }

    /**
     * Resets the specific counter back to zero.
     */
    default void reset(@NotNull K key) {
        set(key, 0L);
    }

    /**
     * Transfers an amount from one key to another.
     * Useful for transactions (e.g., trading points between players).
     */
    default void transfer(@NotNull K from, @NotNull K to, long amount) {
        if (count(from) >= amount) {
            subtract(from, amount);
            add(to, amount);
        }
    }
}