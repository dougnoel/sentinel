package com.dougnoel.sentinel.webdrivers;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import com.dougnoel.sentinel.configurations.Configuration;

public class WebdriverFactoryBadBrowserNameTest {
	
	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws Exception {
		System.clearProperty("browser");
		Configuration.clear("browser");
		Configuration.clear("chromeBrowserBinary");
	}
	
	@Test(expected = WebDriverException.class)
	public void passBadBrowserName() {

		System.setProperty("browser", "badBrowser");
		Configuration.update("browser", "badBrowser");
		WebDriverFactory.instantiateWebDriver();
	}	
	
	//Test IE and Safari Failing based on OS
	@Test(expected = org.openqa.selenium.WebDriverException.class)
	public void failOSSpecificBrowserATest() {
		if(Configuration.operatingSystem().contentEquals("windows")) {
			System.setProperty("browser", "safari");
			Configuration.update("browser", "safari");
			WebDriverFactory.instantiateWebDriver();
		}
		else {
			System.setProperty("browser", "internetexplorer");
			Configuration.update("browser", "internetexplorer");
			WebDriverFactory.instantiateWebDriver();
		}
	}
	
	//Test IE and Safari passing based on OS, passthrough for linux
	//Now bypassing IE as its no longer supported on our execution server.
	@Test
	public void passOSSpecificBrowserTest() {
		var os = Configuration.operatingSystem();
		WebDriver driver;
		switch(os) {
		case "windows":
			Assert.assertTrue("Tested on windows, this is a passthrough test.", true);
			break;
		case "mac":
			System.setProperty("browser", "safari");
			Configuration.update("browser", "safari");
			driver = WebDriverFactory.instantiateWebDriver();
			Assert.assertNotNull(driver);
			WebDriverFactory.quit();
			break;
		default:
			Assert.assertTrue("Tested on linux, this is a passthrough test.", true);
			break;
		}

	}
	
	@Test
	public void passEdgeBrowserTest() {
		var os = Configuration.operatingSystem();
		if(os.contentEquals("windows")) {
			System.setProperty("browser", "edge");
			Configuration.update("browser", "edge");
			var driver = WebDriverFactory.instantiateWebDriver();
			Assert.assertNotNull(driver);
			WebDriverFactory.quit();
		}
		else {
			Assert.assertTrue("Tested on " + os + ", this is a passthrough test.", true);
		}
	}
	
	@Test(expected = org.openqa.selenium.WebDriverException.class)
	public void failEdgeBrowserTest() {
		if(Configuration.operatingSystem().contentEquals("windows")) {
			throw new org.openqa.selenium.WebDriverException();
		}
		else {
			System.setProperty("browser", "edge");
			Configuration.update("browser", "edge");
			WebDriverFactory.instantiateWebDriver();
		}
	}
	
	@Test(expected = WebDriverException.class)
	public void failCustomChromeBrowser() {
		System.setProperty("browser", "customChrome");
		Configuration.update("browser", "customChrome");
		Configuration.update("chromeBrowserBinary", "fakePath");
		WebDriverFactory.instantiateWebDriver();
	}
	
	@Test
	public void passOperaBrowserTest() {
		if (Configuration.toString("headless") != null) {
			Assert.assertTrue("Cannot test Opera in headless mode.", true);
			return;
		}
		System.setProperty("browser", "opera");
		Configuration.update("browser", "opera");
		var driver = WebDriverFactory.instantiateWebDriver();
		Assert.assertNotNull(driver);
		WebDriverFactory.quit();
	}
	
	@Test
	public void passFirefoxBrowserTest() {
		if (Configuration.toString("headless") != null) {
			Assert.assertTrue("Cannot test firefox in headless mode.", true);
			return;
		}
		System.setProperty("browser", "firefox");
		Configuration.update("browser", "firefox");
		var driver = WebDriverFactory.instantiateWebDriver();
		Assert.assertNotNull(driver);
		WebDriverFactory.quit();
	}
}
