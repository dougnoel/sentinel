package com.dougnoel.sentinel.configurations;

import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Time {
	private static final Logger log = LogManager.getLogger(Time.class);
	
	private static Duration timeout = Duration.ZERO;
	private static final Duration interval = Duration.ofMillis(10);
	private static final Duration loopInterval = Duration.ofMillis(100);

	private Time() {
		
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
		if (timeout.isZero()) {
			timeout = Duration.ofSeconds(Configuration.toLong("timeout"));
			if (timeout.isZero()) {
				timeout = Duration.ofSeconds(10);
				log.debug("No timeout property set, using the default timeout value of {} seconds. This can be set in the sentinel.yml config file with a 'timeout=' property or on the command line with the switch '-Dtimeout='.", timeout);
			}
		}
		return timeout;
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
	 * Resets the timeout value so it will be re-read from the configuration.
	 */
	public static void reset() {
		timeout = Duration.ZERO;
		Configuration.clear("timeout");
	}

}
