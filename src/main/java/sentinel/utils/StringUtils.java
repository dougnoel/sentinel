package sentinel.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.message.ParameterizedMessage;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

    public static String format(final String messagePattern, Object... arguments) {
        return ParameterizedMessage.format(messagePattern, arguments);
    }
    
    /**
     * Returns a String for a given InputStream object.
     * <p>
     * https://stackoverflow.com/questions/309424/how-do-i-read-convert-an-inputstream-into-a-string-in-java
     * Answer #3, option #8 which has a significantly faster execution time.
     * @param inputStream the InputStream which contains an incoming String
     * @return String the String representation of the incoming InputStream
     * @throws IOException if the InputStream cannot be opened or read
     */
    public static String inputStreamToString(final InputStream inputStream) throws IOException {
    	ByteArrayOutputStream result = new ByteArrayOutputStream();
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = inputStream.read(buffer)) != -1) {
    	    result.write(buffer, 0, length);
    	}
    	// StandardCharsets.UTF_8.name() > JDK 7
    	return result.toString("UTF-8");
    }
}
