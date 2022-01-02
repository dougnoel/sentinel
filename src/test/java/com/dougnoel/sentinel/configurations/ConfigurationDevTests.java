package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.dougnoel.sentinel.exceptions.ConfigurationNotFoundException;
import com.dougnoel.sentinel.exceptions.SentinelException;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.Driver;

public class ConfigurationDevTests {
	private static String originalEnvironment = null;
	private static final String DEV = "dev";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String DOESNOTEXIST = "Does Not Exist";
	private static final String REGULARUSER = "RegularUser";
	
	@BeforeClass
	public static void setUpBeforeAnyTestsAreRun() throws SentinelException {
		originalEnvironment = Configuration.environment();
		Configuration.environment(DEV);
		System.setProperty("download", "downloads");
		PageManager.setPage("MockTestPage");
	}

	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws Exception {
		Configuration.environment(originalEnvironment);
		Driver.quit();
	}
	
	@Test
	public void loadValueForNonExistentEnvironment() throws SentinelException {
		assertEquals("Expecting the default env RegularUser password", "test", Configuration.accountInformation(REGULARUSER, PASSWORD));
	}
	
	@Test(expected = ConfigurationNotFoundException.class)
	public void failToLoadNonExistentUsernameAndNonExistentEnvironment() throws SentinelException {
		Configuration.accountInformation(DOESNOTEXIST, USERNAME);
	}	
}
