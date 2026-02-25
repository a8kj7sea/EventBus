package me.a8kj.flux.invoker;

import me.a8kj.flux.api.FastInvoker;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class BBInvokerProvider implements InvokerProvider {

    private static final ConcurrentMap<Method, FastInvoker> CACHE = new ConcurrentHashMap<>();
    private static final String PKG = "me.a8kj.flux.generated.Invoker$";

    @Override
    public FastInvoker provide(Method method) {
        return CACHE.computeIfAbsent(method, this::generate);
    }

    @Override
    public void evict(Method method) {
        CACHE.remove(method);
    }

    @Override
    public void clear() {
        CACHE.clear();
    }

    private FastInvoker generate(Method method) {
        try {
            method.setAccessible(true);
            return new ByteBuddy()
                    .subclass(FastInvoker.class)
                    .name(PKG + method.getDeclaringClass().getSimpleName() + "$" + method.getName())
                    .method(ElementMatchers.named("invoke"))
                    .intercept(
                            MethodCall.invoke(method)
                                    .onArgument(0)
                                    .withArgument(1)
                                    .withAssigner(net.bytebuddy.implementation.bytecode.assign.Assigner.DEFAULT,
                                            net.bytebuddy.implementation.bytecode.assign.Assigner.Typing.DYNAMIC)
                    )
                    .make()
                    .load(method.getDeclaringClass().getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Failed to bootstrap bytecode invoker for method: " + method.getName(), e);
        }
    }
}