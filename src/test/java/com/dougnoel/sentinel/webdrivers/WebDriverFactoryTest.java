package com.dougnoel.sentinel.webdrivers;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.dougnoel.sentinel.exceptions.SentinelException;

public class WebDriverFactoryTest {

	private static final String BROWSERVERSION = "browserVersion";
	private static final String CHROME = "chrome";
	private static final String FIREFOX = "firefox";
	
	@Before
	public void setUpBeforeAnyTestsAreRun() throws SentinelException {
		System.clearProperty(BROWSERVERSION);
	}
	
	@Test(expected = org.openqa.selenium.remote.UnreachableBrowserException.class)
	public void createGridShouldFailTest() {
		var gridUrl = "http://gridrul.com";
		System.setProperty(BROWSERVERSION,"1234567");
		GridWebDriverFactory.createGridDriver(CHROME, gridUrl);
	}

// TODO: Selenium Grid not working. Need to setup a self-contained one for testing.
//	@Test
//	public void createGridShouldSucceedTest() {
//		var gridUrl = "http://hub.technologynursery.org/wd/hub";
//		var driver = GridWebDriverFactory.createGridDriver(FIREFOX, gridUrl);
//		try {
//			assertNotNull(driver);
//		}finally {
//			driver.close();
//		}
//	}
//	
//	@Test
//	public void nullBrowserVersionTest() {
//		var gridUrl = "http://hub.technologynursery.org/wd/hub";
//		var driver = GridWebDriverFactory.createGridDriver(CHROME, gridUrl);
//		try {
//			assertNotNull(driver);
//		}finally {
//			driver.close();
//		}
//	}
	
	@Test(expected = com.dougnoel.sentinel.exceptions.MalformedURLException.class)
	public void malFormedUrlTest() {
		System.setProperty(BROWSERVERSION,"86.0");
		var gridUrl = "hub.technologynursery.org:4444/wd/hub";
		GridWebDriverFactory.createGridDriver(FIREFOX, gridUrl);
	}
	
}
