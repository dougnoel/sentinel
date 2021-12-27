package com.dougnoel.sentinel.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Generic Sentinel Exception so that we can throw multiple exception types and 
 * wrap them up into one at the top for cleaner code.
 */
public class SentinelException extends RuntimeException {
	private static final Logger log = LogManager.getLogger(SentinelException.class);
    private static final long serialVersionUID = 8469225740522080236L;

    /**
     * Logs the message and stack trace to the error log.
     * 
     */
    public SentinelException() {
        super();
        log.error(this.getMessage(), this);
    }
    
    public SentinelException(Throwable cause) {
        super(cause);
        log.error(this.getMessage(), this);
    }
    
    public SentinelException(String message) {
        super(message);
        log.error(this.getMessage(), this);
    }

    public SentinelException(String message, Throwable cause) {
        super(message, cause);
        log.error(this.getMessage(), this);
    }
}
