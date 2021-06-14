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
	public void failToLoadSaucelabs() {
		System.setProperty("saucelabsUserName", "FakeName");
		System.setProperty("saucelabsAccessKey", "FakeKey");
		System.setProperty("browserVersion", "1");
		System.setProperty("parent-tunnel", "FakeTunnel");
		System.setProperty("tunnelIdentifier", "FakeTunnelID");
		System.setProperty("name", "Test Name");
		System.setProperty("tags", "Tag1");
		System.setProperty("build", "1.0");
		WebDriverFactory.instantiateWebDriver();
	}
	
	@Test(expected = org.openqa.selenium.WebDriverException.class)
	public void sauceDefaultValues() {
		System.clearProperty("user.name");
		System.setProperty("saucelabsUserName", "FakeName");
		System.setProperty("saucelabsAccessKey", "FakeKey");
		WebDriverFactory.instantiateWebDriver();
	}	
}
