package com.dougnoel.sentinel.exceptions;

public class FileNotFoundException extends ConfigurationNotFoundException {

    /**
     *  Allows us to pass in the stack trace and additional text to a FileNotFoundException
     */
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
