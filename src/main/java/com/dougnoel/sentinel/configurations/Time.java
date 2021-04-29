package com.dougnoel.sentinel.configurations;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Time {
	private static final Logger log = LogManager.getLogger(Time.class);
	
	private static final String TIMEOUT = "timeout";
    private static final String TIMEUNIT = "timeunit";
    private static final String SECONDS = "SECONDS";
	
	private Time() {
		
	}
	
	/**
	 * Wait functionality that takes a double in seconds and converts it to milliseconds before waiting.
	 * @param seconds double number of seconds or fraction thereof (up to milliseconds) to wait.
	 */
	public static void wait(double seconds) {
        wait((long) (seconds * 1000), TimeUnit.MILLISECONDS);
	}
	
	public static void wait(long duration, TimeUnit unit) {
        long milliseconds = TimeUnit.MILLISECONDS.convert(duration, unit);
        log.debug("Passed {} {}, waiting {} milliseconds.", duration, unit, milliseconds);
        try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			log.warn(e.getMessage());
			Thread.currentThread().interrupt();
		}
	}
	
	/**
	 * Returns the value set in the timeout property, or a default of 10 if nothing was configured.
	 * The method unit() is used to determine how the value is measured (seconds, milliseconds, etc).
	 * @return long the timeout
	 * 
	 */
	public static long out() {
		long timeout = Configuration.toLong(TIMEOUT);
		if (timeout == 0L) {
			timeout = 10L;
			Configuration.update(TIMEOUT, timeout);
			log.debug("No timeout property set, using the default timeout value of {}. This can be set in the sentinel.yml config file with a 'timeout=' property or on the command line with the switch '-Dtimeout='.", timeout);
		}
		return timeout;
	}
	
	/**
	 * Returns the timeunit property if it is set for implicit waits, otherwise returns the default
	 * TimeUnit.SECONDS.
	 * Possible return values: DAYS  HOURS, MINUTES, SECONDS, MICROSECONDS, MILLISECONDS, NANOSECONDS
	 * The method out() is used to determine the duration of the timeout.
	 * 
	 * @return java.util.concurrent.TimeUnit the Time Unit
	 */
	public static TimeUnit unit() {
		String unit = StringUtils.upperCase(Configuration.toString(TIMEUNIT));

		if(StringUtils.isEmpty(unit)) {
			log.debug("No timeunit property set, using the default timeunit of SECONDS. This can be set in the sentinel.yml config file with a 'timeunit=' property or on the command line with the switch '-Dtimeunit='. Allowed values are: DAYS, HOURS, MINUTES, SECONDS, MICROSECONDS, MILLISECONDS, NANOSECONDS");
			Configuration.update(TIMEUNIT, SECONDS);
			return TimeUnit.SECONDS;
		}
		switch (unit) {
		case "DAYS":
			return TimeUnit.DAYS;
		case "HOURS":
			return TimeUnit.HOURS;
		case "MINUTES":
			return TimeUnit.MINUTES;
		case SECONDS:
			return TimeUnit.SECONDS;
		case "MICROSECONDS":
			return TimeUnit.MICROSECONDS;
		case "MILLISECONDS":
			return TimeUnit.MILLISECONDS;
		case "NANOSECONDS":
			return TimeUnit.NANOSECONDS;
		default:
			log.error("Timeunit property improperly set as {}, using the default timeunit of SECONDS. This can be set in the sentinel.yml config file with a 'timeunit=' property or on the command line with the switch '-Dtimeunit='. Allowed values are: DAYS, HOURS, MINUTES, SECONDS, MICROSECONDS, MILLISECONDS, NANOSECONDS", unit);
			Configuration.update(TIMEUNIT, SECONDS);
			return TimeUnit.SECONDS;
		}
	}
	
}
