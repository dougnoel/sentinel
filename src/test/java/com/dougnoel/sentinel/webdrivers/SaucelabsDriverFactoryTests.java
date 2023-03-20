package com.dougnoel.sentinel.webdrivers;

import org.junit.AfterClass;
import org.junit.Test;

import com.dougnoel.sentinel.configurations.Configuration;

public class SaucelabsDriverFactoryTests {
	private static String userName = System.getProperty("user.name");
	
	@AfterClass
	public static void tearDownAfterEachTestIsFinished() throws Exception {
		System.setProperty("user.name", userName);
		System.clearProperty("saucelabsUserName");
		Configuration.clear("saucelabsUserName");
		System.clearProperty("saucelabsAccessKey");
		Configuration.clear("saucelabsAccessKey");
		System.clearProperty("browserVersion");
		Configuration.clear("browserVersion");
		System.clearProperty("parent-tunnel");
		Configuration.clear("parent-tunnel");
		System.clearProperty("tunnelIdentifier");
		Configuration.clear("tunnelIdentifier");
		System.clearProperty("name");
		Configuration.clear("name");
		System.clearProperty("tags");
		Configuration.clear("tags");
		System.clearProperty("build");
		Configuration.clear("build");
	}
	
	@Test(expected = org.openqa.selenium.WebDriverException.class)
	public void failToLoadSaucelabsTest() {
		System.setProperty("saucelabsUserName", "FakeName");
		System.setProperty("saucelabsAccessKey", "FakeKey");
		System.setProperty("browserVersion", "1");
		System.setProperty("saucelabsConfigs", "saucelabsConfigs=\"parent-tunnel=\"mytest\", tunnelIdentifier=My-Prd\"");
		System.setProperty("name", "Test Name");
		WebDriverFactory.instantiateWebDriver();
	}
	
	@Test(expected = org.openqa.selenium.WebDriverException.class)
	public void sauceDefaultValuesTest() {
		System.clearProperty("user.name");
		System.setProperty("saucelabsUserName", "FakeName");
		System.setProperty("saucelabsAccessKey", "FakeKey");
		WebDriverFactory.instantiateWebDriver();
	}	
}
