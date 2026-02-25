package me.a8kj.flux.template.concurrent;

/**
 * Functional interfaces that bridge checked exceptions with standard functional logic.
 */
public interface Catching {

    @FunctionalInterface
    interface Runnable {
        void run() throws Exception;
    }

    @FunctionalInterface
    interface Supplier<V> {
        V get() throws Exception;
    }

    @FunctionalInterface
    interface Consumer<T> {
        void accept(T t) throws Exception;
    }
}