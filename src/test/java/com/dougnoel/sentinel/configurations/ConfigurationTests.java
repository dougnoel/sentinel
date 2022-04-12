package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dougnoel.sentinel.exceptions.FileException;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.Driver;

public class ConfigurationTests {
	private static String originalEnvironment = null;
	private static final String STAGE = "stage";
	private static final String DEV = "dev";
	private static final String DEFAULT = "default";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String STAGEUSER = "StageUser";
	private static final String DOESNOTEXIST = "Does Not Exist";
	private static final String REGULARUSER = "RegularUser";
    private static final String LINUX = "linux";
    private static final String MAC = "mac";
    private static final String WINDOWS = "windows";
    private static final String TEST_VALUE = "test_value";
	
	@BeforeClass
	public static void setUpBeforeAnyTestsAreRun() {
		originalEnvironment = Configuration.environment();
		Configuration.environment(STAGE);
		PageManager.setPage("MockTestPage");
	}

	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws Exception {
		Configuration.environment(originalEnvironment);
		Driver.quitAllDrivers();
	}

	@Test
	public void loadDefaultUsername() {
		assertEquals("Expecting the Stage env default username", "DefaultUser", Configuration.accountInformation(DEFAULT, USERNAME));
	}

	@Test
	public void loadDefaultPassword() {
		assertEquals("Expecting the Stage env default password", "MyPassw0rd", Configuration.accountInformation(DEFAULT, PASSWORD));
	}

	@Test
	public void loadUsername() {
		assertEquals("Expecting the Stage env StageUser username", "StageUserName", Configuration.accountInformation(STAGEUSER, USERNAME));
	}

	@Test
	public void loadPassword() {
		assertEquals("Expecting the Stage env StageUser password", "BadPassw0rd", Configuration.accountInformation(STAGEUSER, PASSWORD));
	}

	@Test(expected = FileException.class)
	public void failToLoadNonExistentUsername() {
		Configuration.accountInformation(DOESNOTEXIST, USERNAME);
	}

	@Test(expected = FileException.class)
	public void failToLoadNonExistentPassword() {
		Configuration.accountInformation(DOESNOTEXIST, PASSWORD);
	}
	
	@Test
	public void loadDefaultEnvironmentUsername() {
		assertEquals("Expecting the default env RegularUser username", "test", Configuration.accountInformation(REGULARUSER, USERNAME));
	}

	@Test
	public void loadDefaultEnvironmentPassword() {
		assertEquals("Expecting the default env RegularUser password", "test", Configuration.accountInformation(REGULARUSER, PASSWORD));
	}
	
	@Test
	public void loadValueForNonExistentEnvironment() {
		System.setProperty("env", DEV);
		assertEquals("Expecting the default env RegularUser password", "test", Configuration.accountInformation(REGULARUSER, PASSWORD));
	}
	
	@Test(expected = FileException.class)
	public void failToLoadNonExistentUsernameAndNonExistentEnvironment() {
		System.setProperty("env", DEV);
		Configuration.accountInformation(DOESNOTEXIST, USERNAME);
	}	
	
	@Test
	public void loadStageUrlUsingDefault() {
		assertEquals("Expecting constructed Url.", "http://stage.dougnoel.com/", Configuration.url("DefaultUrls"));
	}
	
	@Test
	public void loadStageUrlUsingBase() {
		assertEquals("Expecting constructed Url.", "http://stage.dougnoel.com/", Configuration.url("BaseUrl"));
	}
	
	@Test(expected = FileException.class)
	public void failToLoadDefaultUrl() {
		Configuration.url("NoDefaultUrl");
	}
	
	@Test(expected = FileException.class)
	public void failToLoadPageWhenFindingUrl() {
		Configuration.url("FakePageObject");
	}

	@Test
	public void iePassedAsBrowser() {
		Configuration.update("browser", "ie");
		assertEquals("Expecting ie to be converted to internetexplorer.", "internetexplorer", Configuration.browser());
		Configuration.clear("browser");
	}

	@Test
	public void windowsPassedAsOS() {
		Configuration.update("os", WINDOWS);
		assertEquals("Expecting os to be set to windows.", WINDOWS, Configuration.operatingSystem());
		Configuration.clear("os");
	}
	
	@Test
	public void windowsDetectedAsOS() {
		String realOS = System.getProperty("os.name");
		System.setProperty("os.name", WINDOWS);
		assertEquals("Expecting os to be detected as windows.", WINDOWS, Configuration.detectOperatingSystem());
		System.setProperty("os.name", realOS);
	}
	
	@Test
	public void macDetectedAsOS() {
		String realOS = System.getProperty("os.name");
		System.setProperty("os.name", MAC);
		assertEquals("Expecting os to be detected as mac.", MAC, Configuration.detectOperatingSystem());
		System.setProperty("os.name", realOS);
	}
	
	@Test
	public void linuxDetectedAsOS() {
		String realOS = System.getProperty("os.name");
		System.setProperty("os.name", LINUX);
		assertEquals("Expecting os to be detected as linux.", LINUX, Configuration.detectOperatingSystem());
		System.setProperty("os.name", realOS);
	}
	
	@Test
	public void otherDetectedAsOS() {
		String realOS = System.getProperty("os.name");
		System.setProperty("os.name", "redhat");
		assertEquals("Expecting os to be detected as redhat.", "redhat", Configuration.detectOperatingSystem());
		System.setProperty("os.name", realOS);
	}
	
	@Test
	public void ToBooleanReturnsFalseTwice() {
		Configuration.update(TEST_VALUE, "false");
		Configuration.toBoolean(TEST_VALUE);
		assertFalse("Expecting test value to be stored as false after the first check and returned as such.", Configuration.toBoolean(TEST_VALUE));
		Configuration.clear(TEST_VALUE);
	}
	
	@Test
	public void TestHasExecutables() {
		Configuration.update(TEST_VALUE, "false");
		Configuration.toBoolean(TEST_VALUE);
		assertFalse("Expecting test value to be stored as false after the first check and returned as such.", Configuration.toBoolean(TEST_VALUE));
		Configuration.clear(TEST_VALUE);
	}
	
	@Test(expected = FileException.class)
	public void failToLoadNonExistentExecutable() {
		Configuration.executable("TextboxPage");
	}
}
