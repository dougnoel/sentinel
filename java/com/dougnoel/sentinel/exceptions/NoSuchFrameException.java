package com.dougnoel.sentinel.exceptions;

/**
 * SentinelException to handle a caught selenium NoSuchFrameException
 */
public class NoSuchFrameException extends SentinelException {

    private static final long serialVersionUID = 8569275741521080263L;

    public NoSuchFrameException(Throwable cause) {
        super(cause);
    }
    
    public NoSuchFrameException(String message) {
        super(message);
    }

    public NoSuchFrameException(String message, Throwable cause) {
        super(message, cause);
    }
}
