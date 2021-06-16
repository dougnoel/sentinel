package com.dougnoel.sentinel.exceptions;

public class PageObjectNotFoundException extends PageNotFoundException {

    /**
     * 
     */
    private static final long serialVersionUID = 7430222770522100336L;

    public PageObjectNotFoundException(Throwable cause) {
        super(cause);
    }
    
    public PageObjectNotFoundException(String message) {
        super(message);
    }

    public PageObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
