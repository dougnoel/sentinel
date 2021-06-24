package com.dougnoel.sentinel.pages;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

public class PageManagerTests {
	
	private static WebDriver driver;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		driver = WebDriverFactory.instantiateWebDriver();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		driver.quit();
	}
	
	@Test(expected = com.dougnoel.sentinel.exceptions.NoSuchWindowException.class)
	public void getWindowHandleInSameWindow() throws InterruptedException {
		PageManager.setPage("Encode DNA Home Page");
		PageManager.switchToNewWindow("Encode DNA New Tab Page");
	}
}
