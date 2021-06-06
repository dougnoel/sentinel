package com.dougnoel.sentinel.webdrivers;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.configurations.Configuration;

public class WebdriverFactoryBadBrowserNameTest {
	
	@AfterClass
	public static void tearDownAfterAllTestsAreFinished() throws Exception {
		System.clearProperty("browser");
		Configuration.clear("browser");
		Configuration.clear("chromeBrowserBinary");
	}
	
	@Test(expected = com.dougnoel.sentinel.exceptions.WebDriverException.class)
	public void passBadBrowserName() {
		System.setProperty("browser", "badBrowser");
		Configuration.update("browser", "badBrowser");
		WebDriverFactory.instantiateWebDriver();
	}	
	
	//Test IE and Safari Failing based on OS
	@Test(expected = org.openqa.selenium.WebDriverException.class)
	public void failOSSpecificBrowserA() {
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
	@Test
	public void passOSSpecificBrowser() {
		var os = Configuration.operatingSystem();
		WebDriver driver;
		switch(os) {
		case "windows":
			System.setProperty("browser", "internetexplorer");
			Configuration.update("browser", "internetexplorer");
			driver = WebDriverFactory.instantiateWebDriver();
			Assert.assertNotNull(driver);
			driver.quit();
			break;
		case "mac":
			System.setProperty("browser", "safari");
			Configuration.update("browser", "safari");
			driver = WebDriverFactory.instantiateWebDriver();
			Assert.assertNotNull(driver);
			driver.quit();
			break;
		default:
			Assert.assertTrue("Tested on linux, this is a passthrough test.", true);
			break;
		}

	}
	
	@Test
	public void passEdgeBrowser() {
		var os = Configuration.operatingSystem();
		if(os.contentEquals("windows")) {
			System.setProperty("browser", "edge");
			Configuration.update("browser", "edge");
			var driver = WebDriverFactory.instantiateWebDriver();
			Assert.assertNotNull(driver);
			driver.quit();
		}
		else {
			Assert.assertTrue("Tested on " + os + ", this is a passthrough test.", true);
		}
	}
	
	@Test(expected = org.openqa.selenium.WebDriverException.class)
	public void failEdgeBrowser() {
		if(Configuration.operatingSystem().contentEquals("windows")) {
			throw new org.openqa.selenium.WebDriverException();
		}
		else {
			System.setProperty("browser", "edge");
			Configuration.update("browser", "edge");
			WebDriverFactory.instantiateWebDriver();
		}
	}
	
	@Test(expected = com.dougnoel.sentinel.exceptions.WebDriverException.class)
	public void failCustomChromeBrowser() {
		System.setProperty("browser", "customChrome");
		Configuration.update("browser", "customChrome");
		Configuration.update("chromeBrowserBinary", "fakePath");
		WebDriverFactory.instantiateWebDriver();
	}
	
	@Test
	public void passOperaBrowser() {
		if (Configuration.toString("headless") != null) {
			Assert.assertTrue("Cannot test Opera in headless mode.", true);
			return;
		}
		System.setProperty("browser", "opera");
		Configuration.update("browser", "opera");
		var driver = WebDriverFactory.instantiateWebDriver();
		Assert.assertNotNull(driver);
		driver.quit();
	}
	
	@Test
	public void passFirefoxBrowser() {
		if (Configuration.toString("headless") != null) {
			Assert.assertTrue("Cannot test firefox in headless mode.", true);
			return;
		}
		System.setProperty("browser", "firefox");
		Configuration.update("browser", "firefox");
		var driver = WebDriverFactory.instantiateWebDriver();
		Assert.assertNotNull(driver);
		driver.quit();
	}
}
