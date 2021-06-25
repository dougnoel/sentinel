package com.dougnoel.sentinel.exceptions;

/**
 *  Allows us to pass in the stack trace and additional text to a MalformedURLException.
 *  Also wraps it in a SentinelException for easier error handling at the glue step level.
 */
public class MalformedURLException extends IOException {

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
