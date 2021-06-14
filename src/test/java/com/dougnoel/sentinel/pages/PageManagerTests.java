package com.dougnoel.sentinel.pages;

import org.junit.AfterClass;
import org.junit.Assert;
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
		pageElement = PageManager.setPage("HomePage");
		baseUrl = Configuration.url();
		PageManager.openPage(baseUrl);
		pageElement.getElement("open_new_window").click();
		PageManager.switchToNewWindow();
		pageElement = PageManager.setPage("OpenNewWindowPage");
		String expectedVal = "A New Popup Window";
		String actualVal = pageElement.getElement("header_text1").getText();
		Assert.assertTrue(actualVal.equals(expectedVal));
	}
	
	@Test
	public void getWindowHandleInNewTab() {
		pageElement = PageManager.setPage("HomePage");
		baseUrl = Configuration.url();
		PageManager.openPage(baseUrl);
		pageElement.getElement("open_new_tab").click();
		PageManager.switchToNewWindow();
		pageElement = PageManager.setPage("OpenNewTabPage");
		String expectedVal = "Window Opened in a New Tab";
		String actualVal = pageElement.getElement("header_text2").getText();
		Assert.assertTrue(actualVal.equals(expectedVal));
	}
}
