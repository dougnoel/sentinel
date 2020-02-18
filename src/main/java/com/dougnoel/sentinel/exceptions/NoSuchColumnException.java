package com.dougnoel.sentinel.exceptions;

/**
 * Thrown when a column requested in a table doesn't exist.
 * @author dougnoel
 *
 */
public class NoSuchColumnException extends ElementNotFoundException {

    private static final long serialVersionUID = 8469225740521080367L;

    public NoSuchColumnException(Throwable cause) {
        super(cause);
    }
    
    public NoSuchColumnException(String message) {
        super(message);
    }

    public NoSuchColumnException(String message, Throwable cause) {
        super(message, cause);
    }
}
