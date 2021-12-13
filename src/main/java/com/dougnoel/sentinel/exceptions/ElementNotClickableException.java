package com.dougnoel.sentinel.exceptions;

/**
 * Thrown when an element is not click-able.
 * @author dougnoel
 *
 */
public class ElementNotClickableException extends SentinelException {

    private static final long serialVersionUID = 6469229747622080236L;

    public ElementNotClickableException(Throwable cause) {
        super(cause);
    }
    
    public ElementNotClickableException(String message) {
        super(message);
    }

    public ElementNotClickableException(String message, Throwable cause) {
        super(message, cause);
    }
}
