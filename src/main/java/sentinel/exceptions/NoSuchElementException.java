package sentinel.exceptions;

public class NoSuchElementException extends SentinelException {

    /**
     * Sentinel handles a captured NoSuchElementException
     */
    private static final long serialVersionUID = 8469225740522080237L;

    public NoSuchElementException(String message) {
        super(message);
    }

    public NoSuchElementException(String message, Throwable cause) {
        super(message, cause);
    }
}
