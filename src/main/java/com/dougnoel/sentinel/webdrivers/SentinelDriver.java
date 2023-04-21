package com.dougnoel.sentinel.webdrivers;

import java.util.LinkedList;
import java.util.List;
import org.openqa.selenium.WebDriver;
import com.dougnoel.sentinel.pages.Page;
import com.dougnoel.sentinel.pages.PageManager;
import io.appium.java_client.windows.WindowsDriver;

/**
 * Tracks all the windows attached to a particular driver.
 */
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

	/**
	 * Returns the webdriver
	 * @return Webdriver the current webdriver
	 */
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
	protected void goToNewWindow() {
		windows.goToNewWindow();
	}

	/**
	 * Wait for, and go to, the window with the same title as the given string.
	 */
	protected void goToTitledWindow(String title) {
		windows.goToTitledWindow(title);
	}

	protected void goToTitledWindowThatContains(String titleContains) {
		windows.goToTitledWindowThatContains(titleContains);
	}

	/**
	 * Checks if a window is found within the session
	 * @param title String the title of the window to check for
	 * @return true if the window is found, false if not
	 */
	protected boolean doesWindowExist(String title) {
		return windows.doesWindowExist(title);
	}

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
			try {
				WindowsDriverFactory.quit((WindowsDriver) driver);
			} catch(org.openqa.selenium.NoSuchSessionException e) {
				//There was an error quitting a windows driver from Appium but the session exception does not handle it due to threading
			}
		else {
			WebDriverFactory.quit();
		}
		pages.clear();
		windows.clear();
	}
}