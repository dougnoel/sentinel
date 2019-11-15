package com.dougnoel.sentinel.exceptions;

public class ConfigurationNotFound extends SentinelException {

    /**
     * 
     */
    private static final long serialVersionUID = 7430222770522100336L;

    public ConfigurationNotFound(String message) {
        super(message);
    }

    public ConfigurationNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
