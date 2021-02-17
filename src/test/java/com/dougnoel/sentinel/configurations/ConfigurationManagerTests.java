package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.exceptions.ConfigurationNotFoundException;
import com.dougnoel.sentinel.exceptions.FileNotFoundException;
import com.dougnoel.sentinel.exceptions.SentinelException;
import com.dougnoel.sentinel.exceptions.URLNotFoundException;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

public class ConfigurationManagerTests {
	private static WebDriver driver;
	
	private static final String STAGE = "stage";
	private static final String DEV = "dev";
	private static final String PROD = "prod";
	private static final String DEFAULT = "default";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String STAGEUSER = "StageUser";
	private static final String DOESNOTEXIST = "Does Not Exist";
	private static final String REGULARUSER = "RegularUser";
	
	@BeforeClass
	public static void setUpBeforeAnyTestsAreRun() throws SentinelException {
		System.setProperty("env", STAGE);
		driver = WebDriverFactory.instantiateWebDriver();
		PageManager.setPage("MockTestPage");
	}
	
	@Before
	public void setUpBeforeEachTest() {
		System.setProperty("env", STAGE);
	}

	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws Exception {
		driver.close();
	}

	@Test
	public void loadDefaultUsername() throws SentinelException {
		assertEquals("Expecting the Stage env default username", "DefaultUser", ConfigurationManager.getAccountInformation(DEFAULT, USERNAME));
	}

	@Test
	public void loadDefaultPassword() throws SentinelException {
		assertEquals("Expecting the Stage env default password", "MyPassw0rd", ConfigurationManager.getAccountInformation(DEFAULT, PASSWORD));
	}

	@Test
	public void loadUsername() throws SentinelException {
		assertEquals("Expecting the Stage env StageUser username", "StageUserName", ConfigurationManager.getAccountInformation(STAGEUSER, USERNAME));
	}

	@Test
	public void loadPassword() throws SentinelException {
		assertEquals("Expecting the Stage env StageUser password", "BadPassw0rd", ConfigurationManager.getAccountInformation(STAGEUSER, PASSWORD));
	}

	@Test(expected = ConfigurationNotFoundException.class)
	public void failToLoadNonExistentUsername() throws SentinelException {
		ConfigurationManager.getAccountInformation(DOESNOTEXIST, USERNAME);
	}

	@Test(expected = ConfigurationNotFoundException.class)
	public void failToLoadNonExistentPassword() throws SentinelException {
		ConfigurationManager.getAccountInformation(DOESNOTEXIST, PASSWORD);
	}
	
	@Test
	public void loadDefaultEnvironmentUsername() throws SentinelException {
		assertEquals("Expecting the default env RegularUser username", "test", ConfigurationManager.getAccountInformation(REGULARUSER, USERNAME));
	}

	@Test
	public void loadDefaultEnvironmentPassword() throws SentinelException {
		assertEquals("Expecting the default env RegularUser password", "test", ConfigurationManager.getAccountInformation(REGULARUSER, PASSWORD));
	}
	
	@Test
	public void loadValueForNonExistentEnvironment() throws SentinelException {
		System.setProperty("env", DEV);
		assertEquals("Expecting the default env RegularUser password", "test", ConfigurationManager.getAccountInformation(REGULARUSER, PASSWORD));
	}
	
	@Test(expected = ConfigurationNotFoundException.class)
	public void failToLoadNonExistentUsernameAndNonExistentEnvironment() throws SentinelException {
		System.setProperty("env", DEV);
		ConfigurationManager.getAccountInformation(DOESNOTEXIST, USERNAME);
	}	
	
	@Test
	public void loadStageUrlUsingDefault() throws SentinelException {
		assertEquals("Expecting constructed Url.", "http://stage.dougnoel.com/", ConfigurationManager.getUrl("DefaultUrls"));
	}
	
	@Test
	public void loadProdUrl() throws SentinelException {
		System.setProperty("env", PROD);
		assertEquals("Expecting loaded Url.", "http://dougnoel.com/", ConfigurationManager.getUrl("DefaultUrls"));
	}
	
	@Test
	public void loadStageUrlUsingBase() throws SentinelException {
		assertEquals("Expecting constructed Url.", "http://stage.dougnoel.com/", ConfigurationManager.getUrl("BaseUrl"));
	}
	
	@Test(expected = URLNotFoundException.class)
	public void failToLoadDefaultUrl() throws SentinelException {
		ConfigurationManager.getUrl("NoDefaultUrl");
	}
	
	@Test(expected = FileNotFoundException.class)
	public void failToLoadPageWhenFindingUrl() throws SentinelException {
		ConfigurationManager.getUrl("FakePageObject");
	}
}
