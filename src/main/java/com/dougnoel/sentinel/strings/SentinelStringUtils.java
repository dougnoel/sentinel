package com.dougnoel.sentinel.strings;

import org.apache.logging.log4j.message.ParameterizedMessage;

public class SentinelStringUtils extends org.apache.commons.lang3.StringUtils {

	/**
	 * Returns a formatted String by replacing each instance of {} place holders  with the given arguments.
	 * @param messagePattern the message pattern containing place holders.
	 * @param arguments the arguments to be used to replace place holders.
	 * @return String the formatted message
	 */
    public static String format(final String messagePattern, Object... arguments) {
        return ParameterizedMessage.format(messagePattern, arguments);
    }

}
