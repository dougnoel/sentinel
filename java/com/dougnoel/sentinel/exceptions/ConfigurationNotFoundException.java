package com.dougnoel.sentinel.exceptions;

public class ConfigurationNotFoundException extends SentinelException {

    /**
     * 
     */
    private static final long serialVersionUID = 7430222770522100336L;

    public ConfigurationNotFoundException(Throwable cause) {
        super(cause);
    }
    
    public ConfigurationNotFoundException(String message) {
        super(message);
    }

    public ConfigurationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
