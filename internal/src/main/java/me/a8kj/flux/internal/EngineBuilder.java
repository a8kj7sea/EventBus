package me.a8kj.flux.internal;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.a8kj.flux.api.Engine;
import me.a8kj.flux.api.Interceptor;
import me.a8kj.flux.invoker.BBInvokerProvider;
import me.a8kj.flux.invoker.InvokerProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EngineBuilder {

    private InvokerProvider invokerProvider;
    private final List<Interceptor> interceptors = new ArrayList<>();

    public static EngineBuilder create() {
        return new EngineBuilder();
    }

    public EngineBuilder invokerProvider(InvokerProvider provider) {
        this.invokerProvider = provider;
        return this;
    }

    public EngineBuilder interceptor(Interceptor interceptor) {
        Objects.requireNonNull(interceptor, "Interceptor cannot be null");
        this.interceptors.add(interceptor);
        return this;
    }

    public Engine build() {
        if (this.invokerProvider == null) {
            this.invokerProvider = new BBInvokerProvider();
        }

        DefaultEngine engine = new DefaultEngine(invokerProvider);
        interceptors.forEach(engine::addInterceptor);

        return engine;
    }
}