package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import java.time.Duration;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class TimeTests {
	private static final String TIMEOUT = "timeout";
	
	@BeforeClass
	public static void setUpBeforeAnyTestsAreRun() {
		Time.reset();
	}
	
	@After
	public void cleanUpBetweenTests() {
		Time.reset();
	}
    
	@Test
	public void defaultTimeoutTest() {
		var previousTimeout = Time.out();
		Time.reset();
		System.clearProperty("timeout");
		assertEquals("Time.out() is using the default when none is set.", Duration.ofSeconds(10), Time.out());
		System.setProperty("timeout", Long.toString(previousTimeout.getSeconds()));
		Configuration.update("timeout", previousTimeout.getSeconds());
	}

	@Test
	public void SetTimeoutTest() {
		Configuration.update(TIMEOUT, 9L);
		assertEquals("Time.out() is using the passed value when set.", Duration.ofSeconds(9), Time.out());
	}
}
