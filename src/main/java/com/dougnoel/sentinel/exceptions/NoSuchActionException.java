package com.dougnoel.sentinel.exceptions;

/**
 * Thrown when an attempt is made to instantiate an Action object that is not defined for the given
 * API object.
 * @author Doug Noël
 *
 */
public class NoSuchActionException extends SentinelException {

    private static final long serialVersionUID = 8469235740522080237L;

    public NoSuchActionException(String message) {
        super(message);
    }

    public NoSuchActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
