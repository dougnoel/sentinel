package sentinel.exceptions;

/**
 * Thrown when a selector type does not exist.
 * @author Doug Noël
 *
 */
public class NoSuchSelectorException extends SentinelException {

    private static final long serialVersionUID = 8469225740521080267L;

    public NoSuchSelectorException(String message) {
        super(message);
    }

    public NoSuchSelectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
