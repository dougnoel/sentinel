package com.dougnoel.sentinel.webdrivers;

import static org.junit.Assert.*;

import org.junit.Test;

public class WebDriverFactoryTest {

	@Test(expected = org.openqa.selenium.remote.UnreachableBrowserException.class)
	public void createGridShouldFailTest() {
		var browser = "chrome";
		var gridUrl = "http://gridrul.com";
		var driver = GridWebDriverFactory.createGridDriver(browser, gridUrl);
		 
		assertNull(driver);
	}
	
	@Test(expected = org.openqa.selenium.remote.UnreachableBrowserException.class)
	public void createGridShouldSucessTest() {
		var browser = "chrome";
		var gridUrl = "http://hub.technologynursery.org:4444/wd/hub";
		var driver = GridWebDriverFactory.createGridDriver(browser, gridUrl);
		 
		assertNotNull(driver);
	}
	
}
