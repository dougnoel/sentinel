package com.dougnoel.sentinel.strings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.openqa.selenium.InvalidArgumentException;

import com.dougnoel.sentinel.configurations.Configuration;

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
    	return result.toString(StandardCharsets.UTF_8);
    }

	/**
	 * Converts the leading integer portion of a string to an int.
	 * <b>Examples:</b>
	 * <ul>
	 *<li>parseOrdinal("12th") = 12</li>
	 *<li>parseOrdinal("1t2h3") = 1</li>
	 *<li>parseOrdinal("ten") throws Exception</li>
	 *</ul>
	 * @param stringStartingWithInteger String a string with leading integers.
	 * @return int the integers in the passed string up until the point of the first non-numeric character.
	 */
	public static int parseOrdinal(String stringStartingWithInteger){
		StringBuilder builder = new StringBuilder();
		stringStartingWithInteger.codePoints()
				.mapToObj(i -> (char)i)
				.takeWhile(CharUtils::isAsciiNumeric)
				.forEach(builder::append);

		return Integer.parseInt(builder.toString());
	}
	
	/**
	 * Returns a string with any strings enclosed in curly braces "{}" replaced with values
	 * stored in the passed YAMLData object under testdata/variables. For example, given the string
	 * "{first_name} Smith" and the yaml entry in the passed file:
	 * 
	 * testdata:
	 *   default:
	 *     variables:
	 *       first_name: Bob
	 *   
	 * This method would return: "Bob Smith".
	 * If there are no values to replace, this method returns the string intact.
	 * @param text String the text to search for variable replacement
	 * @return String the string with variables replaced as applicable
	 */
	public static String replaceVariable(String text) {
		Matcher matcher = Pattern.compile("\\{[^\\}]*+\\}").matcher(text);
		while (matcher.find()) {
			var variable = matcher.group();
			var variableName = StringUtils.substring(variable, 1, -1);
			var value = Configuration.getTestData("variables", variableName);
			text = StringUtils.replaceOnce(text, variable, value); //Using replaceOnce so that if we have the same variable name twice we do not run into iteration issues.
		}
		return text;
	}

	/**
	 * Returns a string with any strings with pattern "\$(int|str) \{.*?\}" replaced with values
	 * stored configuration. For example, given the string
	 {
	 "id": $int {id},
	 "name": "puppy",
	 "category": {
	 "id": 1,
	 "name":  $str {category_name}
	 			}
	 }
	 * This method would return: {
	 * 	 "id": 10,
	 * 	 "name": "puppy",
	 * 	 "category": {
	 * 	 "id": 1,
	 * 	 "name": "Dog"
	 *    }}.
	 * If there are no values to replace, this method returns the string intact.
	 * @param text String the text to search for variable replacement
	 * @return String the string with variables replaced as applicable
	 */
	public static String replaceStoredVariables(String text) {
		Matcher matcher = Pattern.compile("\\$(int|str) \\{.*?\\}").matcher(text);
		while (matcher.find()) {

			var variable = matcher.group();
			var splitted = variable.split(" ");

			var value = Configuration.toString(splitted[1].substring(1, splitted[1].length() - 1));
			if(splitted[0].contains("int")) {
				text = StringUtils.replaceOnce(text, variable, value); //Using replaceOnce so that if we have the same variable name twice we do not run into iteration issues.
			}
			else{
				text = StringUtils.replaceOnce(text, variable, "\""+value+"\""); //Using replaceOnce so that if we have the same variable name twice we do not run into iteration issues.

			}
			}
		return text;
	}
}
