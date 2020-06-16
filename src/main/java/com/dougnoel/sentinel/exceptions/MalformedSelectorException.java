package com.dougnoel.sentinel.exceptions;

/**
 * Thrown when a selector locator string does not exist.
 * @author Doug Noël
 *
 */
public class MalformedSelectorException extends ElementNotFoundException {

    private static final long serialVersionUID = 8469225740574080267L;

    public MalformedSelectorException(Throwable cause) {
        super(cause);
    }
    
    public MalformedSelectorException(String message) {
        super(message);
    }

    public MalformedSelectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
