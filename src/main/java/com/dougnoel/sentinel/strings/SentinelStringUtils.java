package com.dougnoel.sentinel.strings;

import java.util.regex.Pattern;

import org.apache.logging.log4j.message.ParameterizedMessage;

public class SentinelStringUtils extends org.apache.commons.lang3.StringUtils {

	private static final String SURROUNDING_QUOTES = "^[\"'].*[\"']$";
	/**
	 * Returns a formatted String by replacing each instance of {} place holders  with the given arguments.
	 * @param messagePattern the message pattern containing place holders.
	 * @param arguments the arguments to be used to replace place holders.
	 * @return String the formatted message
	 */
    public static String format(final String messagePattern, Object... arguments) {
        return ParameterizedMessage.format(messagePattern, arguments);
    }
    
    /**
     * Returns the string passed without surrounding single (') or double (") quotes
     * if they exist. Otherwise it returns the string unchanged. Quotes inside the string
     * will not be removed. Likewise, if there are not matching quotes on both ends, a leading or
     * trailing quote will be left unmodified.
     * 
     * @param text String the text to check for surrounding quotes
     * @return String the text without surrounding quotes (if they exist) 
     */
    public static String stripSurroundingQuotes(String text) {
    	Pattern pattern = Pattern.compile(SURROUNDING_QUOTES);
    	
    	if (pattern.matcher(text).find())
    		return text.substring(1, text.length()-1);
    	return text;
    }

}
