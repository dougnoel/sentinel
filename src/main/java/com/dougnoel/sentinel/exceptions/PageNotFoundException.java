package com.dougnoel.sentinel.exceptions;

/**
 *  SentinelException to catch error thrown if a Page Object is not found or some operation to get a Page fails
 */
public class PageNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 2756062561461612308L;
	
    public PageNotFoundException(Throwable cause) {
        super(cause);
    }
    
	public PageNotFoundException(String message) {
	    super(message);
	}

	public PageNotFoundException(String message, Throwable cause) {
	    super(message, cause);
	}
	

}
