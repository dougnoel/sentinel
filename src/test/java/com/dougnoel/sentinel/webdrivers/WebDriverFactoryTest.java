package com.dougnoel.sentinel.webdrivers;

import com.dougnoel.sentinel.pages.PageManager;
import org.junit.After;
import org.junit.Test;

import com.dougnoel.sentinel.configurations.Configuration;
import org.openqa.selenium.JavascriptExecutor;
import static junit.framework.TestCase.assertSame;


public class WebDriverFactoryTest {

	private static final String BROWSERVERSION = "browserVersion";
	private static final String FIREFOX = "firefox";
	private static final String GRIDURL = "gridUrl";
	private static final String BROWSER = "browser";
	private static final String CHROME_BROWSER_BINARY = "chromeBrowserBinary";
	
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
		Configuration.clear(CHROME_BROWSER_BINARY);
		System.clearProperty(CHROME_BROWSER_BINARY);
		Configuration.clear("chromeOptions");
		
		Driver.quitAllDrivers();
	}
	
	@Test(expected = com.dougnoel.sentinel.exceptions.MalformedURLException.class)
	public void malFormedUrlTest() {
		System.setProperty(BROWSER, FIREFOX);
		System.setProperty(BROWSERVERSION,"86.0");
		System.setProperty(GRIDURL,"hub.technologynursery.org:4444/wd/hub");
		WebDriverFactory.instantiateWebDriver();
	}
	
	@Test
	public void createChromeOptionsChromeDriver() {
		Configuration.update("chromeOptions", "start-maximized");
		WebDriverFactory.instantiateWebDriver();
		PageManager.setPage("MockTestPage");
		var js = (JavascriptExecutor)Driver.getWebDriver();
		assertSame("Expecting window to be maximized.", "true", js.executeScript("return document.fullscreenEnabled").toString());
	}

	@Test
	public void createChromiumDriver() {
		Configuration.update("chromeOptions", "start-maximized");
		Configuration.update(BROWSER, "chromium");
		// We are assuming, for the sake of the unit tests, that chromium is installed using the Chocolately package
		// manager, which installs the latest "stable" version of chromium for windows.
		// `choco install chromium` installs the chromium binary in the following location.
		Configuration.update(CHROME_BROWSER_BINARY, "C:/Program Files/Chromium/Application/chrome.exe");
		var driver = WebDriverFactory.instantiateWebDriver();

		PageManager.setPage("MockTestPage");
		var js = (JavascriptExecutor)Driver.getWebDriver();
		assertSame("Expecting window to be maximized.", "true", js.executeScript("return document.fullscreenEnabled").toString());
	}

	@Test
	public void createMultipleChromeOptionsChromeDriver() {
		Configuration.update("chromeOptions", "--start-maximized --incognito");
		WebDriverFactory.instantiateWebDriver();
		PageManager.setPage("MockTestPage");
		var js = (JavascriptExecutor)Driver.getWebDriver();
		assertSame("Expecting window to be maximized.", "true", js.executeScript("return document.fullscreenEnabled").toString());
		assertSame("Expecting window to be incognito.", "{}", js.executeScript("return window.webkitRequestFileSystem").toString());
	}
}
