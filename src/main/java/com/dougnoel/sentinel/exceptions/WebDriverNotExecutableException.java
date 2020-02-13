package com.dougnoel.sentinel.exceptions;

/**
 * Thrown when a webdriver is not executable.
 * @author Doug Noël
 *
 */
public class WebDriverNotExecutableException extends WebDriverException {

    private static final long serialVersionUID = 8469225740522181247L;

    public WebDriverNotExecutableException(String message) {
        super(message);
    }

    public WebDriverNotExecutableException(String message, Throwable cause) {
        super(message, cause);
    }
}
