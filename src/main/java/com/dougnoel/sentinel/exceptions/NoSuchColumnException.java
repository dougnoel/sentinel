package com.dougnoel.sentinel.exceptions;

public class NoSuchColumnException extends SentinelException {

    /**
     * 
     */
    private static final long serialVersionUID = 8469225740521080367L;

    public NoSuchColumnException(String message) {
        super(message);
    }

    public NoSuchColumnException(String message, Throwable cause) {
        super(message, cause);
    }
}
