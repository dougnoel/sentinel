package com.dougnoel.sentinel.exceptions;

/**
 * Thrown when an element is disabled and not click-able.
 * @author dougnoel
 *
 */
public class ElementDisabledException extends ElementNotClickableException {

    private static final long serialVersionUID = 6469229798622080236L;

    public ElementDisabledException(Throwable cause) {
        super(cause);
    }
    
    public ElementDisabledException(String message) {
        super(message);
    }

    public ElementDisabledException(String message, Throwable cause) {
        super(message, cause);
    }
}
