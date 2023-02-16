package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;

import com.dougnoel.sentinel.strings.SentinelStringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dougnoel.sentinel.exceptions.FileException;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.system.TestManager;
import com.dougnoel.sentinel.webdrivers.Driver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigurationTests {
	private static final Path CONFIG_FILE_PATH = Path.of("conf/sentinel.yml");
	private static String originalEnvironment = null;
	private static final String ENV = "env";
	private static final String PROD = "prod";
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
		Configuration.update(ENV, STAGE);
		PageManager.setPage("MockTestPage");
	}
	
	@After
	public void tearDownAfterEachTest() {
		PageManager.setPage("MockTestPage");
		Configuration.update(ENV, STAGE);
	}

	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws IOException {
		Configuration.update(ENV, originalEnvironment);
		Driver.quitAllDrivers();
		Files.deleteIfExists(CONFIG_FILE_PATH);
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
	public void getEnvironmentDefault() {
		Configuration.clear(ENV);
		assertEquals("Expecting the default env to be set if none is given.", "localhost", Configuration.environment());
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
		Configuration.update(ENV, DEV);
		assertEquals("Expecting the default env RegularUser password", "test", Configuration.accountInformation(REGULARUSER, PASSWORD));
	}
	
	@Test(expected = FileException.class)
	public void failToLoadNonExistentUsernameAndNonExistentEnvironment() {
		Configuration.update(ENV, DEV);
		Configuration.accountInformation(DOESNOTEXIST, USERNAME);
	}	
	
	@Test
	public void loadStageUrlUsingDefault() {
		PageManager.setPage("DefaultUrls");
		assertEquals("Expecting constructed Url.", "http://stage.dougnoel.com/", Configuration.getURL(TestManager.getActiveTestObject()));
	}
	
	@Test
	public void loadStageUrlUsingBase() {
		PageManager.setPage("BaseUrl");
		assertEquals("Expecting constructed Url.", "http://stage.dougnoel.com/", Configuration.getURL(TestManager.getActiveTestObject()));
	}
	
	@Test
	public void loadProdUrl() {
		Configuration.update(ENV, PROD);
		PageManager.setPage("DefaultUrls");
		assertEquals("Expecting loaded Url.", "http://dougnoel.com/", Configuration.getURL(TestManager.getActiveTestObject()));
	}
	
	@Test(expected = FileException.class)
	public void failToLoadDefaultUrl() {
		PageManager.setPage("NoDefaultUrl");
		Configuration.getURL(TestManager.getActiveTestObject());
	}
	
	@Test(expected = FileException.class)
	public void failToLoadPageWhenFindingUrl() {
		PageManager.setPage("FakePageObject");
		Configuration.getURL(TestManager.getActiveTestObject());
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

	@Test
	public void clearConfiguration(){
		Configuration.update(TEST_VALUE, "true");
		Configuration.clearAllSessionAppProps();
		assertFalse("Expecting test value to default to false due to being cleared out.", Configuration.toBoolean(TEST_VALUE));
	}

	@Test
	public void clearConfigurationDoesNotWipeTimeout(){
		Configuration.clear("timeout");
		System.setProperty("timeout", "3");
		Time.reset();
		Time.out();
		Configuration.clearAllSessionAppProps();
		assertEquals("Expecting timeout to still be 3 after all config values cleared.", Time.out().toSeconds(), 3);
		Configuration.clear("timeout");
		System.clearProperty("timeout");
		Time.reset();
	}

	@Test
	public void clearConfigurationDoesNotWipeEnvironment(){
		Configuration.clear(ENV);
		System.setProperty(ENV, TEST_VALUE);
		Configuration.clearAllSessionAppProps();
		assertEquals(SentinelStringUtils.format("Expecting {} to still be {} after all config values cleared.", ENV, TEST_VALUE), Configuration.environment(), TEST_VALUE);
		Configuration.clear(ENV);
		System.clearProperty(ENV);
	}

	@Test
	public void clearConfigurationDoesNotWipeDataFromConfigFile() throws IOException {
		// - if this test fails for you, it might be because the java version you are using is unable to read the config file (java versions past jdk11).
		//   try adding --add-opens=java.base/java.io=ALL-UNNAMED to your java VM options.

		// - test_value will keep its value from before all config values cleared due to /conf/sentinel.yml file containing 'test_value: 1234' for default env.
		Files.writeString(CONFIG_FILE_PATH,
				"---\n" +
						"configurations:\n" +
						"  default:\n" +
						"    test_value: 1234\n" +
						"...");
		Configuration.clearAllSessionAppProps();
		assertEquals(SentinelStringUtils.format("Expecting {} to still be set after all config values cleared.", TEST_VALUE),
				Configuration.toString(TEST_VALUE), "1234");
	}
}
