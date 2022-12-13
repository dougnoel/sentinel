package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigurationProdTests {
	private static String originalEnvironment = null;
	private static final String ENV = "env";
	private static final String PROD = "prod";
	
	@BeforeClass
	public static void setUpBeforeAnyTestsAreRun() {
		originalEnvironment = Configuration.environment();
		Configuration.update(ENV, PROD);
	}

	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws Exception {
		Configuration.update(ENV, originalEnvironment);
	}
	
	@Test
	public void loadProdUrl() {
		Configuration.update(ENV, PROD);
		assertEquals("Expecting loaded Url.", "http://dougnoel.com/", Configuration.url("DefaultUrls"));
	}
}
