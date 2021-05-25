package com.dougnoel.sentinel.pages;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

public class PageManagerTests {
	
	private static WebDriver driver;
	private static Page pageElement = null;
	private static String baseUrl = null;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		driver = WebDriverFactory.instantiateWebDriver();
		pageElement = PageManager.setPage("SampleTestPage");
        baseUrl = Configuration.url();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		driver.quit();
	}
	
	@Test(expected = com.dougnoel.sentinel.exceptions.NoSuchWindowException.class)
	public void getWindowHandleInSameWindow() {
		PageManager.switchToNewWindow();
	}
	
	@Test
	public void getWindowHandleInNewWindow() {
		PageManager.openPage(baseUrl);
		pageElement.getElement("open_new_window").click();
		PageManager.switchToNewWindow();
	}
	
	@Test
	public void getWindowHandleInNewTab() {
		PageManager.openPage(baseUrl);
		pageElement.getElement("open_new_tab").click();
		PageManager.switchToNewWindow();
	}
}
