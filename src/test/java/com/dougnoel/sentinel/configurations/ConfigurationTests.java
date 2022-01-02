package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.dougnoel.sentinel.exceptions.ConfigurationNotFoundException;
import com.dougnoel.sentinel.exceptions.FileNotFoundException;
import com.dougnoel.sentinel.exceptions.SentinelException;
import com.dougnoel.sentinel.exceptions.URLNotFoundException;
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
	
	@BeforeClass
	public static void setUpBeforeAnyTestsAreRun() throws SentinelException {
		originalEnvironment = Configuration.environment();
		Configuration.environment(STAGE);
		PageManager.setPage("MockTestPage");
	}

	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws Exception {
		Configuration.environment(originalEnvironment);
		Driver.quit();
	}

	@Test
	public void loadDefaultUsername() throws SentinelException {
		assertEquals("Expecting the Stage env default username", "DefaultUser", Configuration.accountInformation(DEFAULT, USERNAME));
	}

	@Test
	public void loadDefaultPassword() throws SentinelException {
		assertEquals("Expecting the Stage env default password", "MyPassw0rd", Configuration.accountInformation(DEFAULT, PASSWORD));
	}

	@Test
	public void loadUsername() throws SentinelException {
		assertEquals("Expecting the Stage env StageUser username", "StageUserName", Configuration.accountInformation(STAGEUSER, USERNAME));
	}

	@Test
	public void loadPassword() throws SentinelException {
		assertEquals("Expecting the Stage env StageUser password", "BadPassw0rd", Configuration.accountInformation(STAGEUSER, PASSWORD));
	}

	@Test(expected = ConfigurationNotFoundException.class)
	public void failToLoadNonExistentUsername() throws SentinelException {
		Configuration.accountInformation(DOESNOTEXIST, USERNAME);
	}

	@Test(expected = ConfigurationNotFoundException.class)
	public void failToLoadNonExistentPassword() throws SentinelException {
		Configuration.accountInformation(DOESNOTEXIST, PASSWORD);
	}
	
	@Test
	public void loadDefaultEnvironmentUsername() throws SentinelException {
		assertEquals("Expecting the default env RegularUser username", "test", Configuration.accountInformation(REGULARUSER, USERNAME));
	}

	@Test
	public void loadDefaultEnvironmentPassword() throws SentinelException {
		assertEquals("Expecting the default env RegularUser password", "test", Configuration.accountInformation(REGULARUSER, PASSWORD));
	}
	
	@Test
	public void loadValueForNonExistentEnvironment() throws SentinelException {
		System.setProperty("env", DEV);
		assertEquals("Expecting the default env RegularUser password", "test", Configuration.accountInformation(REGULARUSER, PASSWORD));
	}
	
	@Test(expected = ConfigurationNotFoundException.class)
	public void failToLoadNonExistentUsernameAndNonExistentEnvironment() throws SentinelException {
		System.setProperty("env", DEV);
		Configuration.accountInformation(DOESNOTEXIST, USERNAME);
	}	
	
	@Test
	public void loadStageUrlUsingDefault() throws SentinelException {
		assertEquals("Expecting constructed Url.", "http://stage.dougnoel.com/", Configuration.url("DefaultUrls"));
	}
	
	@Test
	public void loadStageUrlUsingBase() throws SentinelException {
		assertEquals("Expecting constructed Url.", "http://stage.dougnoel.com/", Configuration.url("BaseUrl"));
	}
	
	@Test(expected = URLNotFoundException.class)
	public void failToLoadDefaultUrl() throws SentinelException {
		Configuration.url("NoDefaultUrl");
	}
	
	@Test(expected = FileNotFoundException.class)
	public void failToLoadPageWhenFindingUrl() throws SentinelException {
		Configuration.url("FakePageObject");
	}

	@Test
	public void iePassedAsBrowser() throws SentinelException {
		Configuration.update("browser", "ie");
		assertEquals("Expecting ie to be converted to internetexplorer.", "internetexplorer", Configuration.browser());
		Configuration.clear("browser");
	}

	@Test
	public void windowsPassedAsOS() throws SentinelException {
		Configuration.update("os", WINDOWS);
		assertEquals("Expecting os to be set to windows.", WINDOWS, Configuration.operatingSystem());
		Configuration.clear("os");
	}
	
	@Test
	public void windowsDetectedAsOS() throws SentinelException {
		String realOS = System.getProperty("os.name");
		System.setProperty("os.name", WINDOWS);
		assertEquals("Expecting os to be detected as windows.", WINDOWS, Configuration.detectOperatingSystem());
		System.setProperty("os.name", realOS);
	}
	
	@Test
	public void macDetectedAsOS() throws SentinelException {
		String realOS = System.getProperty("os.name");
		System.setProperty("os.name", MAC);
		assertEquals("Expecting os to be detected as mac.", MAC, Configuration.detectOperatingSystem());
		System.setProperty("os.name", realOS);
	}
	
	@Test
	public void linuxDetectedAsOS() throws SentinelException {
		String realOS = System.getProperty("os.name");
		System.setProperty("os.name", LINUX);
		assertEquals("Expecting os to be detected as linux.", LINUX, Configuration.detectOperatingSystem());
		System.setProperty("os.name", realOS);
	}
	
	@Test
	public void otherDetectedAsOS() throws SentinelException {
		String realOS = System.getProperty("os.name");
		System.setProperty("os.name", "redhat");
		assertEquals("Expecting os to be detected as redhat.", "redhat", Configuration.detectOperatingSystem());
		System.setProperty("os.name", realOS);
	}
}
