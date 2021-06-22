package com.dougnoel.sentinel.exceptions;

/**
 *  SentinelException to catch error thrown if a URL is not found or some operation to get a URL fails
 */
public class URLNotFoundException extends ConfigurationNotFoundException {

    private static final long serialVersionUID = 8460245760821180367L;

    public URLNotFoundException(String message) {
        super(message);
    }

    public URLNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
