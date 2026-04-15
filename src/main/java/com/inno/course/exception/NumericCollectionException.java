package com.inno.course.exception;

/**
 * Custom exception class for numeric collection operations.
 * Thrown when errors occur during collection creation, manipulation, or validation.
 */
public class NumericCollectionException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     *
     * @param message the detail message
     */
    public NumericCollectionException(String message) {
        super(message);
    }

    /**
     * Constructs a new exception with the specified detail message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public NumericCollectionException(String message, Throwable cause) {
        super(message, cause);
    }
}