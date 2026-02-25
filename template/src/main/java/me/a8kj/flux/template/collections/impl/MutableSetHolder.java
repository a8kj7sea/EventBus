package me.a8kj.flux.template.collections.impl;


import me.a8kj.flux.template.collections.MutableHolder;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class MutableSetHolder<T> implements MutableHolder<T> {

    private final Set<T> elements;

    public MutableSetHolder() {
        this.elements = new CopyOnWriteArraySet<>();
    }

    public MutableSetHolder(Collection<T> initial) {
        this.elements = new CopyOnWriteArraySet<>(initial);
    }

    @Override
    public boolean contains(T element) {
        return elements.contains(element);
    }

    @Override
    public void add(T element) {
        elements.add(element);
    }

    @Override
    public void remove(T element) {
        elements.remove(element);
    }

    @Override
    public void update(Collection<T> collection) {
        elements.clear();
        elements.addAll(collection);
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @Override
    public Optional<T> any() {
        return elements.stream().findAny();
    }

    @Override
    public Optional<T> first() {
        return elements.stream().findFirst();
    }

    @Override
    public Collection<T> elements() {
        return Collections.unmodifiableSet(elements);
    }
}
