package me.a8kj.eventbus.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to mark a method as an event subscriber in an event bus
 * system.
 * Methods annotated with {@code @EventSubscribe} will be invoked when the
 * corresponding event is posted to the event bus.
 * <p>
 * The annotation allows additional metadata to control the behavior of event
 * subscription,
 * such as whether the event handler should run asynchronously and a description
 * of the event handler.
 * </p>
 * 
 * @author a8kj7sea
 * @since 1.0
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface EventSubscribe {

    /**
     * Optional description of the event handler method.
     * This can be used to provide more context about what the method does or what
     * kind of events it handles.
     * 
     * @return the description of the event handler method
     */
    String description() default "";

    /**
     * Indicates whether the event handler should be executed asynchronously.
     * If set to {@code true}, the method will be invoked asynchronously when the
     * event is posted.
     * 
     * @return {@code true} if the event handler should be asynchronous,
     *         {@code false} otherwise
     */
    boolean async() default false;
}
