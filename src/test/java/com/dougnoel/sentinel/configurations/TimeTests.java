package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import java.time.Duration;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TimeTests {
	private static final String TIMEOUT = "timeout";
	private static Duration previousTimeout;
	
	@Before
	public void setUpBetweenTests() {
		previousTimeout = Time.out();
	}
	
	@After
	public void cleanUpBetweenTests() {
		Time.setTimeout(previousTimeout);
		Configuration.clear(TIMEOUT);
	}
    
	@Test
	public void defaultTimeoutTest() {
		System.clearProperty(TIMEOUT);
		Time.initializeTimeOut();
		assertEquals("Time.out() is using the default when none is set.", Duration.ofSeconds(10), Time.out());
	}

	@Test
	public void SetTimeoutTest() {
		Configuration.update(TIMEOUT, 9L);
		Time.initializeTimeOut();
		assertEquals("Time.out() is using the passed value when set.", Duration.ofSeconds(9), Time.out());
	}
}
