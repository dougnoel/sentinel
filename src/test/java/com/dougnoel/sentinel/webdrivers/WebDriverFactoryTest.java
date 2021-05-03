package com.dougnoel.sentinel.webdrivers;

import static org.junit.Assert.*;
import org.junit.Test;

public class WebDriverFactoryTest {

	@Test(expected = org.openqa.selenium.remote.UnreachableBrowserException.class)
	public void createGridShouldFailTest() {
		var browser = "chrome";
		var gridUrl = "http://gridrul.com";
		System.setProperty("browserVersion","1234567");
		GridWebDriverFactory.createGridDriver(browser, gridUrl);
	}
	
	@Test
	public void createGridShouldSucessTest() {
		var browser = "firefox";
		System.setProperty("browserVersion","86.0");
		var gridUrl = "http://hub.technologynursery.org/wd/hub";
		var driver = GridWebDriverFactory.createGridDriver(browser, gridUrl);
		try {
			assertNotNull(driver);
		}finally {
			driver.quit();;
		}
	}
	
	@Test(expected = com.dougnoel.sentinel.exceptions.MalformedURLException.class)
	public void malFormedUrlTest() {
		var browser = "firefox";
		System.setProperty("browserVersion","86.0");
		var gridUrl = "hub.technologynursery.org:4444/wd/hub";
		GridWebDriverFactory.createGridDriver(browser, gridUrl);
		
	}
	
}
