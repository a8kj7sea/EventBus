package me.a8kj.flux.template.capability;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.a8kj.flux.template.Pair;
import me.a8kj.flux.template.Switchable;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
public class DirectBitmask<K> implements Bitmask<K> {

    private final Map<K, Switchable> flagsMap = new ConcurrentHashMap<>();

    @Setter
    private long rawMask = 0L;

    @Override
    public void register(@NonNull K key, @NonNull Switchable value) {
        flagsMap.put(key, value);
    }

    @Override
    public void unregister(@NonNull K key) {
        flagsMap.remove(key);
    }

    @Override
    public boolean hasEntry(@NonNull K key) {
        return flagsMap.containsKey(key);
    }

    @Override
    public @NonNull Optional<Switchable> get(@NonNull K key) {
        return Optional.ofNullable(flagsMap.get(key));
    }

    @Override
    public @NonNull Map<K, Switchable> asMap() {
        return java.util.Collections.unmodifiableMap(flagsMap);
    }

    @Override
    public @NonNull Iterable<Pair<K, Switchable>> entries() {
        return flagsMap.entrySet().stream()
                .map(e -> new Pair<K, Switchable>(e.getKey(), e.getValue()))
                .collect(Collectors.toUnmodifiableList());
    }
}