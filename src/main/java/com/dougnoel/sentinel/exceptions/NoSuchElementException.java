package com.dougnoel.sentinel.exceptions;

/**
 * Thrown if an Element does not exist on a webpage.
 * @author dougnoel
 *
 */

public class NoSuchElementException extends ElementNotFoundException {

    private static final long serialVersionUID = 8469225740522080237L;

    public NoSuchElementException(Throwable cause) {
        super(cause);
    }
    
    public NoSuchElementException(String message) {
        super(message);
    }

    public NoSuchElementException(String message, Throwable cause) {
        super(message, cause);
    }
}
