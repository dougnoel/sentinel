package com.dougnoel.sentinel.exceptions;

public class MissingConfigurationException extends SentinelException {

    /**
     * SentinelException to handle missing Sentinel configuration properties
     */
    private static final long serialVersionUID = 3495972112538381803L;

    public MissingConfigurationException(String message) {
        super(message);
    }

    public MissingConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
