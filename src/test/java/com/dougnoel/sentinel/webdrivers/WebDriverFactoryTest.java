package com.dougnoel.sentinel.webdrivers;

import com.dougnoel.sentinel.pages.PageManager;

import static org.junit.Assume.assumeFalse;
import static org.junit.Assume.assumeTrue;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.dougnoel.sentinel.configurations.Configuration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import static junit.framework.TestCase.assertSame;


public class WebDriverFactoryTest {

    private static final String MAC = "mac";
    private static final String WINDOWS = "windows";
    private static final String HEADLESS = "headless";
	private static final String BROWSER = "browser";
	private static final String SAFARI = "safari";
	private static final String EDGE = "edge";
	private static final String FIREFOX = "firefox";
	private static final String OPERA = "opera";
	private static final String CUSTOM_CHROME = "customChrome";
	private static final String CHROME_BROWSER_BINARY = "chromeBrowserBinary";
	private static final String CHROME_OPTIONS = "chromeOptions";
	private static final String BAD_DATA = "badData";
	private static final String BROWSERVERSION = "browserVersion";
	private static final String GRIDURL = "gridUrl";
	
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
		Configuration.clear(CHROME_OPTIONS);
		
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
		Configuration.update(CHROME_OPTIONS, "start-maximized");
		WebDriverFactory.instantiateWebDriver();
		PageManager.setPage("MockTestPage");
		var js = (JavascriptExecutor)Driver.getWebDriver();
		assertSame("Expecting window to be maximized.", "true", js.executeScript("return document.fullscreenEnabled").toString());
	}

	@Test
	public void createMultipleChromeOptionsChromeDriver() {
		Configuration.update(CHROME_OPTIONS, "--start-maximized --incognito");
		WebDriverFactory.instantiateWebDriver();
		PageManager.setPage("MockTestPage");
		var js = (JavascriptExecutor)Driver.getWebDriver();
		assertSame("Expecting window to be maximized.", "true", js.executeScript("return document.fullscreenEnabled").toString());
		assertSame("Expecting window to be incognito.", "{}", js.executeScript("return window.webkitRequestFileSystem").toString());
	}
	
	@Test(expected = WebDriverException.class)
	public void passBadBrowserName() {
		System.setProperty(BROWSER, BAD_DATA);
		Configuration.update(BROWSER, BAD_DATA);
		WebDriverFactory.instantiateWebDriver();
	}	
	
	@Test(expected = WebDriverException.class)
	public void failSafariOnWindows() {
		assumeTrue(Configuration.operatingSystem().contentEquals(WINDOWS));
		System.setProperty(BROWSER, SAFARI);
		Configuration.update(BROWSER, SAFARI);
		WebDriverFactory.instantiateWebDriver();
	}
	
	@Test(expected = WebDriverException.class)
	public void failEdgeOnNonWindows() {
		assumeFalse(Configuration.operatingSystem().contentEquals(WINDOWS));
		System.setProperty(BROWSER, EDGE);
		Configuration.update(BROWSER, EDGE);
		WebDriverFactory.instantiateWebDriver();
	}
	
	@Test
	public void passSafariOnMac() {
		assumeTrue(Configuration.operatingSystem().contentEquals(MAC));
		System.setProperty(BROWSER, SAFARI);
		Configuration.update(BROWSER, SAFARI);
		WebDriver driver = WebDriverFactory.instantiateWebDriver();
		Assert.assertNotNull(driver);
	}
	
	@Test
	public void passEdgeOnWindows() {
		assumeTrue(Configuration.operatingSystem().contentEquals(WINDOWS));
		System.setProperty(BROWSER, EDGE);
		Configuration.update(BROWSER, EDGE);
		WebDriver driver = WebDriverFactory.instantiateWebDriver();
		Assert.assertNotNull(driver);
	}
	
	@Test(expected = WebDriverException.class)
	public void failCustomChromeBrowser() {
		System.setProperty(BROWSER, CUSTOM_CHROME);
		Configuration.update(BROWSER, CUSTOM_CHROME);
		Configuration.update(CHROME_BROWSER_BINARY, BAD_DATA);
		WebDriverFactory.instantiateWebDriver();
	}
	
	@Test
	public void passOperaBrowser() {
		assumeFalse(Configuration.toString(HEADLESS) != null); //Cannot test Opera in headless mode
		System.setProperty(BROWSER, OPERA);
		Configuration.update(BROWSER, OPERA);
		WebDriver driver = WebDriverFactory.instantiateWebDriver();
		Assert.assertNotNull(driver);
	}
	
	@Test
	public void passFirefoxBrowser() {
		assumeFalse(Configuration.toString(HEADLESS) != null); //Cannot test Firefox in headless mode
		System.setProperty(BROWSER, FIREFOX);
		Configuration.update(BROWSER, FIREFOX);
		WebDriver driver = WebDriverFactory.instantiateWebDriver();
		Assert.assertNotNull(driver);
	}
}
