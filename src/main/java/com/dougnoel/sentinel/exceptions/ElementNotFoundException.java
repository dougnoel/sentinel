package com.dougnoel.sentinel.exceptions;

/**
 * Thrown when an element is not found. Wraps a number of more specific reasons.
 * @author dougnoel
 *
 */
public class ElementNotFoundException extends SentinelException {

    private static final long serialVersionUID = 6469229740522080236L;

    public ElementNotFoundException(Throwable cause) {
        super(cause);
    }
    
    public ElementNotFoundException(String message) {
        super(message);
    }

    public ElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
