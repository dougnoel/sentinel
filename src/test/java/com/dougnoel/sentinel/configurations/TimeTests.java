package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class TimeTests {
	private static final String TIMEOUT = "timeout";
    private static final String TIMEUNIT = "timeunit";
    
	@Test
	public void timeoutTest() {
		assertEquals("Time.out() is using the default when none is set.", 10L, Time.out());
		Configuration.update(TIMEOUT, 9L);
		assertEquals("Time.out() is using the passed value when set.", 9L, Time.out());
	}
	
	@Test
	public void timeunitTest() {
		assertEquals("Time.unit() is using SECONDS as the default when none is set.", TimeUnit.SECONDS, Time.unit());
		Configuration.update(TIMEUNIT, "Days");
		assertEquals("Time.unit() is using the updated value of Days.", TimeUnit.DAYS, Time.unit());
		Configuration.update(TIMEUNIT, "hours");
		assertEquals("Default timeunit", TimeUnit.HOURS, Time.unit());
		Configuration.update(TIMEUNIT, "MINUTES");
		assertEquals("Default timeunit", TimeUnit.MINUTES, Time.unit());
		Configuration.update(TIMEUNIT, "MICROSECONDS");
		assertEquals("Default timeunit", TimeUnit.MICROSECONDS, Time.unit());
		Configuration.update(TIMEUNIT, "MILLISECONDS");
		assertEquals("Default timeunit", TimeUnit.MILLISECONDS, Time.unit());
		Configuration.update(TIMEUNIT, "NANOSECONDS");
		assertEquals("Default timeunit", TimeUnit.NANOSECONDS, Time.unit());
		Configuration.update(TIMEUNIT, "BADVALUE");
		assertEquals("Default timeunit", TimeUnit.SECONDS, Time.unit());
		Configuration.update(TIMEUNIT, "SECONDS");
		assertEquals("Default timeunit", TimeUnit.SECONDS, Time.unit());
	}

}
