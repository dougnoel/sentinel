package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.dougnoel.sentinel.exceptions.SentinelException;

public class ConfigurationManagerProdTests {
	private static String originalEnvironment = null;
	private static final String PROD = "prod";
	
	@BeforeClass
	public static void setUpBeforeAnyTestsAreRun() throws SentinelException {
		originalEnvironment = ConfigurationManager.getEnvironment();
		ConfigurationManager.setEnvironment(PROD);
	}

	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws Exception {
		ConfigurationManager.setEnvironment(originalEnvironment);
	}
	
	@Test
	public void loadProdUrl() throws SentinelException {
		ConfigurationManager.setEnvironment(PROD);
		assertEquals("Expecting loaded Url.", "http://dougnoel.com/", ConfigurationManager.getUrl("DefaultUrls"));
	}
}
