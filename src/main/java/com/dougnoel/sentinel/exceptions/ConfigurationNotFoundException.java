package com.dougnoel.sentinel.exceptions;

/**
 * Exception thrown if a configuration value cannot be found in the configuration file or on the command line.
 */
public class ConfigurationNotFoundException extends SentinelException {

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
