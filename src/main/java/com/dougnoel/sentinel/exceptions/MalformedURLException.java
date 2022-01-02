package com.dougnoel.sentinel.exceptions;

/**
 *  Wrapping java.net.MalformedURLException so that it is a RuntimeException.
 */
public class MalformedURLException extends RuntimeException {

    private static final long serialVersionUID = 8461266760821180367L;

    /**
     * Creates an exception that will not halt execution.
     */
    public MalformedURLException() {
        super();
    }
    
    /**
     * Takes a Throwable. Creates an exception that will not halt execution.
     * 
     * @param cause Throwable the exception that caused this exception to be thrown
     */
    public MalformedURLException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Takes a message. Creates an exception that will not halt execution.
     * 
     * @param message String the message for this exception
     */
    public MalformedURLException(String message) {
        super(message);
    }

    /**
     * Takes a message and a Throwable. Creates an exception that will not halt execution.
     * 
     * @param message String the message for this exception
     * @param cause Throwable the exception that caused this exception to be thrown
     */
    public MalformedURLException(String message, Throwable cause) {
        super(message, cause);
    }
}
