package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.exceptions.ConfigurationNotFoundException;
import com.dougnoel.sentinel.exceptions.SentinelException;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

public class ConfigurationManagerTests {
	private static WebDriver driver;
	
	private static final String STAGE = "stage";
	private static final String DEV = "dev";
	private static final String DEFAULT = "default";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
	private static final String STAGEUSER = "StageUser";
	private static final String DOESNOTEXIST = "Does Not Exist";
	private static final String REGULARUSER = "RegularUser";
	
	@BeforeClass
	public static void setUpBeforeAnyTestsAreRun() throws SentinelException {
		System.setProperty("env", STAGE);
		System.setProperty("pageObjectPackages", "com.demoaut");
		driver = WebDriverFactory.instantiateWebDriver();
		PageManager.setPage("NewToursLoginPage");
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
		assertEquals("Expecting the Stage env default username", "DefaultUser", ConfigurationManager.getUsernameOrPassword(DEFAULT, USERNAME));
	}

	@Test
	public void loadDefaultPassword() throws SentinelException {
		assertEquals("Expecting the Stage env default password", "MyPassw0rd", ConfigurationManager.getUsernameOrPassword(DEFAULT, PASSWORD));
	}

	@Test
	public void loadUsername() throws SentinelException {
		assertEquals("Expecting the Stage env StageUser username", "StageUserName", ConfigurationManager.getUsernameOrPassword(STAGEUSER, USERNAME));
	}

	@Test
	public void loadPassword() throws SentinelException {
		assertEquals("Expecting the Stage env StageUser password", "BadPassw0rd", ConfigurationManager.getUsernameOrPassword(STAGEUSER, PASSWORD));
	}

	@Test(expected = ConfigurationNotFoundException.class)
	public void failToLoadNonExistentUsername() throws SentinelException {
		ConfigurationManager.getUsernameOrPassword(DOESNOTEXIST, USERNAME);
	}

	@Test(expected = ConfigurationNotFoundException.class)
	public void failToLoadNonExistentPassword() throws SentinelException {
		ConfigurationManager.getUsernameOrPassword(DOESNOTEXIST, PASSWORD);
	}
	
	@Test
	public void loadDefaultEnvironmentUsername() throws SentinelException {
		assertEquals("Expecting the default env RegularUser username", "test", ConfigurationManager.getUsernameOrPassword(REGULARUSER, USERNAME));
	}

	@Test
	public void loadDefaultEnvironmentPassword() throws SentinelException {
		assertEquals("Expecting the default env RegularUser password", "test", ConfigurationManager.getUsernameOrPassword(REGULARUSER, PASSWORD));
	}
	
	@Test
	public void loadValueForNonExistentEnvironment() throws SentinelException {
		System.setProperty("env", DEV);
		assertEquals("Expecting the default env RegularUser password", "test", ConfigurationManager.getUsernameOrPassword(REGULARUSER, PASSWORD));
	}
	
	@Test(expected = ConfigurationNotFoundException.class)
	public void failToLoadNonExistentUsernameAndNonExistentEnvironment() throws SentinelException {
		System.setProperty("env", DEV);
		ConfigurationManager.getUsernameOrPassword(DOESNOTEXIST, USERNAME);
	}	
	
//	urls:
//		   base: http://newtours.demoaut.com/
//		accounts:
//		   default:
//		      RegularUser:
//		         username: test
//		         password: test
//		      BadUser:
//		         username: test
//		         password: test
//		   stage:
//		      default:
//		         username: DefaultUser
//		         password: MyPassw0rd
//		      StageUser:
//		         username: StageUserName
//		         password: BadPassw0rd
}
