package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dougnoel.sentinel.exceptions.SentinelException;

public class ConfigurationManagerDefaultTests {
	private static String originalEnvironment = null;
	
	@BeforeClass
	public static void setUpBeforeAnyTestsAreRun() throws SentinelException {
		originalEnvironment = ConfigurationManager.getEnvironment();
		ConfigurationManager.setEnvironment(null);
	}

	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws Exception {
		ConfigurationManager.setEnvironment(originalEnvironment);
	}
	
	@Test
	public void getEnvironmentDefault() {
		assertEquals("Expecting the default env to be set if none is given.", "localhost", ConfigurationManager.getEnvironment());
	}
}
