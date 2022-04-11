package com.dougnoel.sentinel.webdrivers;

import org.junit.After;
import org.junit.Test;

import com.dougnoel.sentinel.configurations.Configuration;

public class WebDriverFactoryTest {

	private static final String BROWSERVERSION = "browserVersion";
	private static final String FIREFOX = "firefox";
	private static final String GRIDURL = "gridUrl";
	private static final String BROWSER = "browser";
	
//	@BeforeClass
//	public static void setUpBeforeAnyTestsAreRun() throws SentinelException {
//		System.clearProperty(BROWSERVERSION);
//	}
	
	@After
	public void tearDownAfterEachTest() {
		Configuration.clear(BROWSERVERSION);
		System.clearProperty(BROWSERVERSION);
		Configuration.clear(GRIDURL);
		System.clearProperty(GRIDURL);
		Configuration.clear(BROWSER);
		System.clearProperty(BROWSER);
		Configuration.clear("chromeBrowserBinary");
		System.clearProperty("chromeBrowserBinary");
		
		WebDriverFactory.quit();
	}
	
	@Test(expected = org.openqa.selenium.remote.UnreachableBrowserException.class)
	public void createGridShouldFailTest() {
		System.setProperty(BROWSERVERSION,"1234567");
		System.setProperty(GRIDURL,"http://gridrul.com");
		WebDriverFactory.instantiateWebDriver();
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
		System.setProperty(BROWSER, FIREFOX);
		System.setProperty(BROWSERVERSION,"86.0");
		System.setProperty(GRIDURL,"hub.technologynursery.org:4444/wd/hub");
		WebDriverFactory.instantiateWebDriver();
	}
	
	@Test(expected = org.openqa.selenium.WebDriverException.class)
	public void createChromeOptionsChromeDriver() {
		Configuration.update("chromeOptions", "start-maximized");
		WebDriverFactory.instantiateWebDriver();
	}
}
