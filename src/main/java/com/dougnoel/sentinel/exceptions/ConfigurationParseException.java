package com.dougnoel.sentinel.exceptions;

public class ConfigurationParseException extends ConfigurationNotFoundException {

    /**
     * 
     */
    private static final long serialVersionUID = 1460220740522180836L;

    public ConfigurationParseException(String message) {
        super(message);
    }

    public ConfigurationParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
