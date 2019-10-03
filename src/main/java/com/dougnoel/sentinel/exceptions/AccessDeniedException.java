package com.dougnoel.sentinel.exceptions;

public class AccessDeniedException extends SentinelException {

    /**
     *  Allows us to pass in the stack trace and additional text to a AccessDeniedException.
     *  Also wraps it in a SentinelException for easier error handling at the glue step level.
     */
    private static final long serialVersionUID = 8461275760821180367L;

    public AccessDeniedException(Throwable cause) {
        super(cause);
    }
    
    public AccessDeniedException(String message) {
        super(message);
    }

    public AccessDeniedException(String message, Throwable cause) {
        super(message, cause);
    }
}
