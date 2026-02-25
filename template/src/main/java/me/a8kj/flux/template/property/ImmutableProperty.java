package me.a8kj.flux.template.property;

/**
 * A property that represents a constant state.
 * The value is typically provided during instantiation.
 */
public interface ImmutableProperty<V> extends Property<V> {
    // This interface acts as a marker for thread-safety and stability.
}