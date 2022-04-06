package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.dougnoel.sentinel.exceptions.FileException;
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
	public static void setUpBeforeAnyTestsAreRun() {
		originalEnvironment = Configuration.environment();
		Configuration.environment(DEV);
		System.setProperty("download", "downloads");
		PageManager.setPage("MockTestPage");
	}

	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws Exception {
		Configuration.environment(originalEnvironment);
		Driver.quitAllDrivers();
	}
	
	@Test
	public void loadValueForNonExistentEnvironment() {
		assertEquals("Expecting the default env RegularUser password", "test", Configuration.accountInformation(REGULARUSER, PASSWORD));
	}
	
	@Test(expected = FileException.class)
	public void failToLoadNonExistentUsernameAndNonExistentEnvironment() {
		Configuration.accountInformation(DOESNOTEXIST, USERNAME);
	}	
}
