package com.dougnoel.sentinel.configurations;

import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Time {
	private static final Logger log = LogManager.getLogger(Time.class);
	
	private static Duration timeout = Duration.ZERO;
	private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(10);
	private static final Duration DEFAULT_LONG_TIMEOUT = Duration.ofSeconds(60);
	private static final Duration interval = Duration.ofMillis(10);
	private static final Duration loopInterval = Duration.ofMillis(100);

	private static Duration longProcessTimeout = Duration.ZERO;

	private Time() {
		
	}
	
	static {
		initializeTimeOut();
		initalizeLongProcessTimeout();
	}
	
	/**
	 * Checks to see if a custom timeout value is configured. If so, that is the timeout used
	 * to initialize all timeout checks. Otherwise the DEFAULT_TIMEOUT is used.
	 */
	protected static void initializeTimeOut() {
		timeout = getTimeoutInitializationValue("timeout", DEFAULT_TIMEOUT);
	}
	
	/**
	 * Checks to see if a custom longProcessTimeout value is configured. If so, that is the timeout used
	 * to initialize all longProcessTimeout checks. Otherwise the DEFAULT_LONG_TIMEOUT is used.
	 */
	protected static void initalizeLongProcessTimeout() {
		longProcessTimeout = getTimeoutInitializationValue("longProcessTimeout", DEFAULT_LONG_TIMEOUT);
	}
	
	/**
	 * Common code for setting timeouts.
	 * 
	 * @param timeOutName String the name of the timeout value to read and write
	 * @param defaultValue String the default value to use if the value isn't set.
	 * @return Duration the value is returned to be set to the internal value for tracking.
	 */
	private static Duration getTimeoutInitializationValue(String timeOutName, Duration defaultValue) {
		var timeout = Duration.ofSeconds(Configuration.toLong(timeOutName));
		if (timeout.isZero()) {
			timeout = defaultValue;
			log.debug("No {} property set, using the default timeout value of {} seconds. This can be set in the sentinel.yml config file with a '{}=' property or on the command line with the switch '-Dtimeout='.", timeOutName, timeout, timeOutName);
		}
		return timeout;
	}
	
	/**
	 * Wait functionality that takes a double in seconds and converts it to milliseconds before waiting.
	 * @param seconds double number of seconds or fraction thereof (up to milliseconds 10^-3) to wait.
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
	 * Returns the value set in the timeout property in seconds, or a default of 10 seconds
	 * if nothing was configured.
	 * @return Duration the timeout as a Java Duration object that can be used to get different values
	 * 
	 */
	public static Duration out() {
		return timeout;
	}

	/**
	 * Returns the value set in the longProcessTimeout property in seconds, or a default of 60 seconds
	 * if nothing was configured.
	 * @return Duration the timeout as a Java Duration object that can be used to get different values
	 *
	 */
	public static Duration longProcessTimeout(){
		return longProcessTimeout;
	}
	
	/**
	 * Returns 10 milliseconds in a Duration object for the interval between element searches.
	 * @return Duration the interval to wait between searches
	 */
	public static Duration interval() {
		return interval;
	}

	/**
	 * Returns 100 milliseconds in a Duration object for the interval between element searches and interactions.
	 * @return Duration the interval to loop for when searching / interacting with elements.
	 */
	public static Duration loopInterval() {
		return loopInterval;
	}

	/**
	 * Set the timeout to a new value for unit testing.
	 * 
	 * @param Duration long the amount of time to set the timeout to
	 */
	protected static void setTimeout(Duration time) {
		timeout = time;
	}

}
