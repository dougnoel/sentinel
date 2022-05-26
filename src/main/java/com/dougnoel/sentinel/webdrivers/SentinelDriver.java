package com.dougnoel.sentinel.webdrivers;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.pages.Page;
import com.dougnoel.sentinel.pages.PageManager;

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
	
	protected WebDriver getWebDriver() {
		return this.driver;
	}

	/**
	 * Go to next window.
	 */
    protected void goToNextWindow() {
    	windows.goToNextWindow();
    }

	/**
	 * Wait for, and go to, next window.
	 */
	protected void goToNewWindow() { windows.goToNewWindow(); }
    
    /**
     * Go to previous window.
     */
    protected void goToPreviousWindow() {
    	windows.goToPreviousWindow();
    }
    
	/**
	 * Emulate clicking the browser's forward button.
	 */
	protected void navigateForward() {
		driver.navigate().forward();
	}

	/**
	 * Emulate clicking the browser's back button.
	 */
	protected void navigateBack() {
		driver.navigate().back();
	}

	/**
	 * Emulate clicking the browser's refresh button.
	 */
	protected void refresh() {
		driver.navigate().refresh();
	}
	
	/**
	 * Maximize the window.
	 */
    protected void maximizeWindow() {
    	driver.manage().window().maximize();
    }
	
	/**
	 * Close the current window.
	 */
	protected void close() {
		windows.closeCurrentWindow();
	}
	
	/**
	 * Quit the driver and cleanup.
	 */
	@SuppressWarnings("unchecked")
	protected void quit() {
		if (driver.getClass().getSimpleName().contentEquals("WindowsDriver"))
			WinAppDriverFactory.quit((WindowsDriver<WindowsElement>) driver);
		else {
			WebDriverFactory.quit();
		}
		pages.clear();
		windows.clear();
	}
}