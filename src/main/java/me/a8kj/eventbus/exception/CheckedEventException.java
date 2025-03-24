package me.a8kj.eventbus.exception;

/**
 * Custom exception class representing a checked exception in event handling.
 * This exception can be thrown when there is an issue with event processing.
 * It extends the {@link Exception} class and provides constructors for
 * different use cases.
 * 
 * @author a8kj7sea
 * @since 1.0
 */
public class CheckedEventException extends Exception {

    /** The cause of the exception. */
    private final Throwable cause;

    /**
     * Constructs a new {@code CheckedEventException} with the specified cause.
     *
     * @param throwable The cause of the exception, which can be retrieved later
     *                  using the {@link #getCause()} method.
     */
    public CheckedEventException(Throwable throwable) {
        super(throwable);
        this.cause = throwable;
    }

    /**
     * Constructs a new {@code CheckedEventException} with no detailed message or
     * cause.
     */
    public CheckedEventException() {
        this.cause = null;
    }

    /**
     * Constructs a new {@code CheckedEventException} with a specified detail
     * message and cause.
     *
     * @param cause   The cause of the exception, which can be retrieved later using
     *                the {@link #getCause()} method.
     * @param message The detail message, which can be retrieved later using the
     *                {@link #getMessage()} method.
     */
    public CheckedEventException(Throwable cause, String message) {
        super(message, cause);
        this.cause = cause;
    }

    /**
     * Constructs a new {@code CheckedEventException} with the specified detail
     * message.
     *
     * @param message The detail message, which can be retrieved later using the
     *                {@link #getMessage()} method.
     */
    public CheckedEventException(String message) {
        super(message);
        this.cause = null;
    }

    /**
     * Returns the cause of this exception, or {@code null} if the cause is
     * nonexistent or unknown.
     *
     * @return The cause of this exception, or {@code null}.
     */
    @Override
    public Throwable getCause() {
        return this.cause;
    }

    /**
     * Returns the detail message string of this throwable.
     * This method uses the {@link Exception#getMessage()} method to retrieve the
     * message.
     *
     * @return The detail message string of this throwable.
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
