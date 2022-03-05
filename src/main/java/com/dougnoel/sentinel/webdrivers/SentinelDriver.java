package com.dougnoel.sentinel.webdrivers;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.framework.PageManager;
import com.dougnoel.sentinel.pages.Page;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;

public class SentinelDriver {
	//The webdriver
	private WebDriver driver;
	//The windows  for the driver
	private WindowList windows;
	//The page objects used in order
	private List<Page> pages = new LinkedList<>();
	//Current page we are on

	SentinelDriver(WebDriver driver) {
		this.driver = driver;
		windows = new WindowList(driver);
		pages.add(PageManager.getPage());
	}
	
	//Find out if this driver has been used for a particular page object
	
	protected WebDriver getWebDriver() {
		return this.driver;
	}
	
	//close window
	protected void close() {
		windows.closeCurrentWindow();
	}
	
	protected void quit() {
		if (driver.getClass().getSimpleName().contentEquals("WindowsDriver"))
			WinAppDriverFactory.quit(castWindowsDriver(driver));
		else {
			WebDriverFactory.quit();
		}
	}
	
    /**
     * Takes a WebDriver object and casts it to a WindowsDriver object.
     * <p>
     * Note: This method does no type checking.
     * 
     * @param driver WebDriver the WebDriver to be cast
     * @return WindowsDriver&lt;WebElement&gt; the cast WebDriver object
     */
    @SuppressWarnings("unchecked")
	private static WindowsDriver<WindowsElement> castWindowsDriver(WebDriver driver) {
    	return (WindowsDriver<WindowsElement>) driver;
    }
}