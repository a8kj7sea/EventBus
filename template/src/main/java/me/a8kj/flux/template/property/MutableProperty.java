package me.a8kj.flux.template.property;

/**
 * A property whose value can be updated after initialization.
 */
public interface MutableProperty<V> extends Property<V> {

    void setValue(V value);

    /**
     * Updates the value and returns this property for fluent API usage.
     */
    default MutableProperty<V> withValue(V value) {
        setValue(value);
        return this;
    }
}