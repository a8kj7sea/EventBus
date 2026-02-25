package me.a8kj.flux.invoker;

import me.a8kj.flux.api.FastInvoker;

import java.lang.reflect.Method;

public interface InvokerProvider {
    FastInvoker provide(Method method);

    void evict(Method method);

    void clear();
}
