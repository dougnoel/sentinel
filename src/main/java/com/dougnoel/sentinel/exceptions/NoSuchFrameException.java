package com.dougnoel.sentinel.exceptions;

public class NoSuchFrameException extends SentinelException {

    /**
     * SentinelException to handle a caught selenium NoSuchFrameException
     */
    private static final long serialVersionUID = 8569275741521080263L;

    public NoSuchFrameException(String message) {
        super(message);
    }

    public NoSuchFrameException(String message, Throwable cause) {
        super(message, cause);
    }
}
