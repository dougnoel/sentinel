package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dougnoel.sentinel.exceptions.SentinelException;

public class ConfigurationDefaultTests {
	private static String originalEnvironment = null;
	
	@BeforeClass
	public static void setUpBeforeAnyTestsAreRun() throws SentinelException {
		originalEnvironment = Configuration.environment();
		Configuration.environment(null);
	}

	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws Exception {
		Configuration.environment(originalEnvironment);
	}
	
	@Test
	public void getEnvironmentDefault() {
		assertEquals("Expecting the default env to be set if none is given.", "localhost", Configuration.environment());
	}
}
