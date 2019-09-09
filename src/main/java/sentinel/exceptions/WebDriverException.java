package sentinel.exceptions;

public class WebDriverException extends SentinelException {

	/**
	 * Sentinel Exception to handle errors thrown when instantiating the WebDriverFactory
	 */
	private static final long serialVersionUID = 472010903809534872L;

	public WebDriverException(String message) {
		super(message);
	}
	
	public WebDriverException(String message, Throwable cause) {
		super(message);
	}

}
