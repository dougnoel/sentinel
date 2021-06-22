package com.dougnoel.sentinel.exceptions;

public class ConfigurationParseException extends ConfigurationNotFoundException {

    /**
     * Thrown when a configuration file cannot be parsed.
     */
    private static final long serialVersionUID = 1460220740522180836L;

    public ConfigurationParseException(String message) {
        super(message);
    }

    public ConfigurationParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
