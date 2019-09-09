package sentinel.utils;

/**
 * Possible methods for an API to authenticate.
 * 
 * @author Doug Noël
 *
 */
public enum AuthenticationType {
	NONE,
	JWT,
	AUTH_KEY;
}