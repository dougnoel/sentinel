package com.dougnoel.sentinel.configurations;

import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import com.dougnoel.sentinel.strings.StringUtils;

public class TimeoutManager {
	private static final Logger log = LogManager.getLogger(TimeoutManager.class); // Create a logger.

	private static long timeout = createDefaultTimeout();
	private static TimeUnit timeunit = createDefaultTimeUnit();
	
	/**
	 * Sets the implicit timeout for the passed driver.
	 * 
	 * @param time long the number of seconds to wait before reporting a failure to
	 *             find an element
	 * @param unit java.util.concurrent.TimeUnit the unit of time to wait (e.g.
	 *             seconds, milliseconds, etc)
	 * @return Timeouts returns to allow object chaining for more complex calls
	 */
	public static void setTimeout(WebDriver driver, long time, TimeUnit unit) {
		driver.manage().timeouts().implicitlyWait(time, unit);
	}
	
	/**
	 * Sets the implicit timeout for the current driver. Calls the Configuration
	 * Manager to see if the default timeout or default time unit have been changed
	 * through a property. If not, 10 seconds will be set as the default.
	 */
	public static void setDefaultTimeout(WebDriver driver) {
		setTimeout(driver, getDefaultTimeout(), getDefaultTimeUnit());
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
		TimeUnit timeunit = TimeUnit.SECONDS;

		if(StringUtils.isEmpty(unit)) {
			log.info("No timeunit property set, using the default timeunit of SECONDS. This can be set in the sentinel.yml config file with a 'timeunit=' property or on the command line with the switch '-Dtimeunit='. Allowed values are: DAYS, HOURS, MINUTES, SECONDS, MICROSECONDS, MILLISECONDS, NANOSECONDS");
			return timeunit;
		}
		switch (unit) {
		case "DAYS":
			timeunit = TimeUnit.DAYS;
			break;
		case "HOURS":
			timeunit = TimeUnit.HOURS;
			break;
		case "MINUTES":
			timeunit = TimeUnit.MINUTES;
			break;
		case "SECONDS":
			timeunit = TimeUnit.SECONDS;
			break;
		case "MICROSECONDS":
			timeunit = TimeUnit.MICROSECONDS;
			break;
		case "MILLISECONDS":
			timeunit = TimeUnit.MILLISECONDS;
			break;
		case "NANOSECONDS":
			timeunit = TimeUnit.NANOSECONDS;
			break;
		}
		log.info("Timeunit property set to {}.", unit);
		return timeunit;
	}
}
