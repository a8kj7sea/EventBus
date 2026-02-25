package me.a8kj.flux.template.collections;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;


public interface Holder<S> {

    default Collection<S> snapshot() {
        return Collections.unmodifiableCollection(elements());
    }

    Collection<S> elements();

    default Stream<S> stream() {
        return snapshot().stream();
    }

    default int size() {
        return snapshot().size();
    }
}