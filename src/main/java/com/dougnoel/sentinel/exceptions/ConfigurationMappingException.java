package com.dougnoel.sentinel.exceptions;

public class ConfigurationMappingException extends ConfigurationNotFoundException {

    /**
     * Thrown when a configuration file cannot be mapped correctly.
     */
    private static final long serialVersionUID = 7430222710522100336L;

    public ConfigurationMappingException(String message) {
        super(message);
    }

    public ConfigurationMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
