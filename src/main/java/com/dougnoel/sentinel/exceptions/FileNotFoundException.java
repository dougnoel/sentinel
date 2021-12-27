package com.dougnoel.sentinel.exceptions;

/**
 *  Allows us to pass in the stack trace and additional text to a FileNotFoundException
 */
public class FileNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 8461245760821180367L;

    public FileNotFoundException(Throwable cause) {
        super(cause);
    }
    
    public FileNotFoundException(String message) {
        super(message);
    }

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
