package sentinel.exceptions;

public class NoSuchWindowException extends SentinelException {

    /**
     * SentinelException to handle a caught Selenium NoSuchWindowException
     */
    private static final long serialVersionUID = 8429725740521083267L;

    public NoSuchWindowException(String message) {
        super(message);
    }

    public NoSuchWindowException(String message, Throwable cause) {
        super(message, cause);
    }
}
