package me.a8kj.flux.template.capability;


import lombok.NonNull;
import me.a8kj.flux.template.Switchable;
import me.a8kj.flux.template.registry.Registry;

import java.util.Optional;

public interface Bitmask<K> extends Registry<K, Switchable> {


    long getRawMask();


    void setRawMask(long mask);


    default void setEnabled(@NonNull K key, boolean state) {
        get(key).ifPresent(option -> {
            long currentMask = getRawMask();
            int bit = option.getBit();

            if (state) {
                setRawMask(currentMask | (1L << bit));  // enable
            } else {
                setRawMask(currentMask & ~(1L << bit)); // disable
            }
        });
    }

    default boolean isEnabled(@NonNull K key) {
        Optional<Switchable> optionOpt = get(key);

        if (optionOpt.isEmpty()) {
            //  Logger.warn("Setting " + key + " not found!");
            return false;
        }

        int bit = optionOpt.get().getBit();
        return (getRawMask() & (1L << bit)) != 0;
    }
}
