package com.dougnoel.sentinel.configurations;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TimeoutManager {
	private static final Logger log = LogManager.getLogger(TimeoutManager.class); // Create a logger.

	private static long timeout = createDefaultTimeout();
	private static TimeUnit timeunit = createDefaultTimeUnit();
	
	private TimeoutManager() {
		
	}
	
	/**
	 * Wait functionality that takes a double in seconds and converts it to milliseconds before waiting.
	 * @param seconds double number of seconds or fraction thereof (up to milliseconds) to wait.
	 */
	public static void wait(double seconds) {
        long milliseconds = (long) (seconds * 1000);
        log.debug("Passed {} seconds, waiting {} milliseconds.", seconds, milliseconds);
        try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			log.warn(e.getMessage());
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * Returns the value set in the timeout property.
	 * The method getDefaultTimeUnit is used to determine how the value is measured (seconds, milliseconds, etc).
	 * @return long the timeout
	 * 
	 */
	public static long getDefaultTimeout() {
		return TimeoutManager.timeout;
	}
	
	/**
	 * Returns the timeunit property if it is set for implicit waits, otherwise returns the default.
	 * Possible return values: DAYS  HOURS, MINUTES, SECONDS, MICROSECONDS, MILLISECONDS, NANOSECONDS
	 * The default if the value is not set is TimeUnit.SECONDS.
	 * The method getDefaultTimeout is used to determine the duration of the timeout.
	 * 
	 * @return java.util.concurrent.TimeUnit the default value
	 */
	public static TimeUnit getDefaultTimeUnit() {
		return timeunit;
	}
	
	/**
	 * Sets the timeout property value by reading it from the config file or from the command line.
	 * The default if the property is not set is 10.
	 * The method getDefaultTimeUnit is used to determine how the value is measured (seconds, milliseconds, etc).
	 * @return long the timeout
	 * 
	 */
	private static long createDefaultTimeout() {
		long timeout = 10L;
		String timeoutProp = ConfigurationManager.getOptionalProperty("timeout");
		if(StringUtils.isNotEmpty(timeoutProp)) {
			timeout = Long.parseLong(timeoutProp);
			log.info("Timeout property set to {}.", timeoutProp);
		} else {
			log.info("No timeout property set, using the default timeout value of {}. This can be set in the sentinel.yml config file with a 'timeout=' property or on the command line with the switch '-Dtimeout='.", timeout);
		}
		return timeout;
	}
	
	/**
	 * Returns the timeunit property if it is set for implicit waits, otherwise returns the default.
	 * Possible return values: DAYS, HOURS, MINUTES, SECONDS, MICROSECONDS, MILLISECONDS, NANOSECONDS
	 * The default if the value is not set is TimeUnit.SECONDS.
	 * The method getDefaultTimeout is used to determine the duration of the timeout.
	 * 
	 * @return java.util.concurrent.TimeUnit the default value
	 */
	private static TimeUnit createDefaultTimeUnit() {
		String unit = StringUtils.capitalize(ConfigurationManager.getOptionalProperty("timeunit"));

		if(StringUtils.isEmpty(unit)) {
			log.info("No timeunit property set, using the default timeunit of SECONDS. This can be set in the sentinel.yml config file with a 'timeunit=' property or on the command line with the switch '-Dtimeunit='. Allowed values are: DAYS, HOURS, MINUTES, SECONDS, MICROSECONDS, MILLISECONDS, NANOSECONDS");
			return TimeUnit.SECONDS;
		}
		log.info("Timeunit property set to {}.", unit);
		switch (unit) {
		case "DAYS":
			return TimeUnit.DAYS;
		case "HOURS":
			return TimeUnit.HOURS;
		case "MINUTES":
			return TimeUnit.MINUTES;
		case "SECONDS":
			return TimeUnit.SECONDS;
		case "MICROSECONDS":
			return TimeUnit.MICROSECONDS;
		case "MILLISECONDS":
			return TimeUnit.MILLISECONDS;
		case "NANOSECONDS":
			return TimeUnit.NANOSECONDS;
		default:
			return TimeUnit.SECONDS;
		}
	}
}
