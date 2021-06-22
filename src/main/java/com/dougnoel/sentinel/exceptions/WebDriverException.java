package com.dougnoel.sentinel.exceptions;

/**
 * Sentinel Exception to handle errors thrown when instantiating the WebDriverFactory
 */
public class WebDriverException extends SentinelException {

	private static final long serialVersionUID = 472010903809534872L;
	
    public WebDriverException(Throwable cause) {
        super(cause);
    }

	public WebDriverException(String message) {
		super(message);
	}
	
	public WebDriverException(String message, Throwable cause) {
		super(message);
	}

}
