package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.dougnoel.sentinel.exceptions.SentinelException;

public class ConfigurationProdTests {
	private static String originalEnvironment = null;
	private static final String PROD = "prod";
	
	@BeforeClass
	public static void setUpBeforeAnyTestsAreRun() throws SentinelException {
		originalEnvironment = Configuration.environment();
		Configuration.environment(PROD);
	}

	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws Exception {
		Configuration.environment(originalEnvironment);
	}
	
	@Test
	public void loadProdUrl() throws SentinelException {
		Configuration.environment(PROD);
		assertEquals("Expecting loaded Url.", "http://dougnoel.com/", Configuration.url("DefaultUrls"));
	}
}
