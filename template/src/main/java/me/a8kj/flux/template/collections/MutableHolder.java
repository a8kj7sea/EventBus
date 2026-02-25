package me.a8kj.flux.template.collections;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MutableHolder<T> extends Holder<T> {

    boolean contains(T element);

    void add(T element);

    void remove(T element);

    void update(Collection<T> collection);

    default void set(int index, T element) {
        Collection<T> current = elements();
        if (current instanceof List<?> list) {
            @SuppressWarnings("unchecked")
            List<T> typedList = (List<T>) list;
            typedList.set(index, element);
        } else {
            throw new IllegalCallerException("Cannot call set on a non-list holder");
        }
    }


    void clear();

    Optional<T> any();

    Optional<T> first();
}