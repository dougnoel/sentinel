package com.dougnoel.sentinel.exceptions;

/**
 * SentinelException to handle a caught Selenium NoSuchWindowException
 */
public class NoSuchWindowException extends SentinelException {

    private static final long serialVersionUID = 8429725740521083267L;

    public NoSuchWindowException(String message) {
        super(message);
    }

    public NoSuchWindowException(String message, Throwable cause) {
        super(message, cause);
    }
}
