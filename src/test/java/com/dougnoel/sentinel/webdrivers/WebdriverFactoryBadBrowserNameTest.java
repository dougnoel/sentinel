package com.dougnoel.sentinel.webdrivers;

import org.junit.AfterClass;
import org.junit.Test;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.WebDriverException;

public class WebdriverFactoryBadBrowserNameTest {
	
	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws Exception {
		System.clearProperty("browser");
		Configuration.clear("browser");
	}
	
	@Test(expected = WebDriverException.class)
	public void passBadBrowserName() {
		System.setProperty("browser", "badBrowser");
		WebDriverFactory.instantiateWebDriver();
	}	
}
