package com.dougnoel.sentinel.exceptions;

/**
 * Thrown if an Element is accessed as a type other than it is.
 * @author dougnoel
 *
 */

public class ElementTypeMismatchException extends SentinelException {

    private static final long serialVersionUID = 8469225740522520237L;

    public ElementTypeMismatchException(Throwable cause) {
        super(cause);
    }
    
    public ElementTypeMismatchException(String message) {
        super(message);
    }

    public ElementTypeMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
