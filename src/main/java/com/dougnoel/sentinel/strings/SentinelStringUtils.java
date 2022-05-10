package com.dougnoel.sentinel.strings;

import java.util.regex.Pattern;

import org.apache.logging.log4j.message.ParameterizedMessage;
import org.openqa.selenium.InvalidArgumentException;

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
    	if (text == null)
    		return null;
    	Pattern pattern = Pattern.compile(SURROUNDING_QUOTES);
    	
    	if (pattern.matcher(text).find())
    		return text.substring(1, text.length()-1);
    	return text;
    }

	/**
	 * Constructs the ordinal string of the given int. For example "1st", "2nd", "111th"
	 * @param i int the integer to create an ordinal string for
	 * @return String the ordinal string
	 */
	public static String ordinal(int i) {
		if(i < 0)
			throw new InvalidArgumentException("Integer must be non-negative to produce ordinal.");

		String[] suffixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
		switch (i % 100) {
			case 11:
			case 12:
			case 13:
				return i + "th";
			default:
				return i + suffixes[i % 10];
		}
	}

}
