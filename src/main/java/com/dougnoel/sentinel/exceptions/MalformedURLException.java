package com.dougnoel.sentinel.exceptions;

/**
 *  Wrapping MalformedURLException so that it is a RuntimeException.
 */
public class MalformedURLException extends RuntimeException {

    private static final long serialVersionUID = 8461266760821180367L;

    public MalformedURLException(Throwable cause) {
        super(cause);
    }
    
    public MalformedURLException(String message) {
        super(message);
    }

    public MalformedURLException(String message, Throwable cause) {
        super(message, cause);
    }
}
