package com.dougnoel.sentinel.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Reports an issue if the session for Appium is unavailable.
 * @author Paul Turchinetz
 *
 */
public class NoSuchSessionException extends RuntimeException {
    private static final long serialVersionUID = 8469235740522080238L;
    private static final Logger log = LogManager.getLogger(FileException.class);

    public NoSuchSessionException(String message) {
        super(message);
        log.trace(message);
    }
}
