package sentinel.exceptions;

public class ConfigurationMappingException extends SentinelException {

    /**
     * 
     */
    private static final long serialVersionUID = 7430222710522100336L;

    public ConfigurationMappingException(String message) {
        super(message);
    }

    public ConfigurationMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
