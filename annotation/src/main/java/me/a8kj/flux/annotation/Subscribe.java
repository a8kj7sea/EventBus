package me.a8kj.flux.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {
    Execution mode() default Execution.VIRTUAL;

    int priority() default 0;

    boolean ignoreCancelled() default false;
}
