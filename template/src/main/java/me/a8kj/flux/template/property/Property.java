package me.a8kj.flux.template.property;

/**
 * A named container for a value.
 */
public interface Property<V> {

    String getName();

    V getValue();

    /**
     * Checks if the property has a non-null value.
     */
    default boolean hasValue() {
        return getValue() != null;
    }
}