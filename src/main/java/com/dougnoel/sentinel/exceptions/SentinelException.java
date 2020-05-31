package com.dougnoel.sentinel.exceptions;

public class SentinelException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 8469225740522080236L;

    public SentinelException(Throwable cause) {
        super(cause);
    }
    
    public SentinelException(String message) {
        super(message);
    }

    public SentinelException(String message, Throwable cause) {
        super(message, cause);
    }
}
