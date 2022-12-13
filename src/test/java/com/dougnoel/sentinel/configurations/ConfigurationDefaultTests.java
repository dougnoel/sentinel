package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigurationDefaultTests {
	private static final String ENV = "env";
	private static String originalEnvironment = null;
	
	@BeforeClass
	public static void setUpBeforeAnyTestsAreRun() {
		originalEnvironment = Configuration.environment();
		Configuration.clear(ENV);
	}

	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws Exception {
		Configuration.update(ENV, originalEnvironment);
	}
	
	@Test
	public void getEnvironmentDefault() {
		Configuration.initializeEnvironment();
		assertEquals("Expecting the default env to be set if none is given.", "localhost", Configuration.environment());
	}
}