package com.dougnoel.sentinel.webdrivers;

import java.util.EnumMap;
import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.pages.PageManager;

import org.openqa.selenium.WebDriver;

/**
 * Manages all drivers. Maintains as few drivers as possible for test execution.
 * Currently we store one driver for browsers and one for Windows. We can add an appium driver here as well.
 */
public class Driver {
	private static EnumMap<PageObjectType, SentinelDriver> drivers = new EnumMap<> (PageObjectType.class);
	
	/**
	 * Exists only to defeat instantiation.
	 */
	private Driver() {
    }

    /**
     * Returns the SentinelDriver instance. This will silently instantiate the WebDriver if that has not been done yet.
     * 
     * @return SentinelDriver the created SentinelDriver
     */
    private static SentinelDriver getSentinelDriver() {
    	PageObjectType pageObjectType = PageManager.getPage().getPageObjectType();
    	SentinelDriver currentDriver = null;
    	if (pageObjectType == PageObjectType.EXECUTABLE) {
    		currentDriver = drivers.computeIfAbsent(pageObjectType, driver -> new SentinelDriver(WinAppDriverFactory.createWinAppDriver()));
    	}
    		else {
    		currentDriver = drivers.computeIfAbsent(pageObjectType, driver -> new SentinelDriver(WebDriverFactory.getWebDriver()));
        }
    	return currentDriver;
    }
	
    /**
     * Returns the WebDriver instance. This will silently instantiate the WebDriver if that has not been done yet.
     * 
     * @return WebDriver the created Selenium WebDriver object
     */
    public static WebDriver getWebDriver() {
    	return getSentinelDriver().getWebDriver();
    }

    /**
     * Quits all drivers and removes them from the list of active drivers.
     */
    public static void quitAllDrivers() {
    	drivers.forEach((driverType, driver) -> driver.quit());
    	drivers.clear();
    }
    
    /**
     * Closes the current window and moves the driver to the previous window.
     */
    public static void closeWindow() {
    	getSentinelDriver().close();
    }
    
    /**
     * Asks the current driver to move forwards to the next window in the list. Use of this 
     * method assumes that the user knows the expected state of the software they are testing 
     * and will test to make sure they are in the correct window.
     */
    public static void goToNextWindow() {
    	getSentinelDriver().goToNextWindow();
    }

	/**
	 * Asks the current driver to wait for, and move forwards to, the next new window in the list.
	 * <br><br>Use of this method assumes:
	 * <br>That the user knows the expected state of the software they are testing,
	 * and will test to make sure they are in the correct window.
	 */
	public static void goToNewWindow() {
		getSentinelDriver().goToNewWindow();
	}
    
    /**
     * Asks the current driver to move backwards to the previous window in the list. Use of
     * this method assumes that the user knows the expected state of the software they are 
     * testing and will test to make sure they are in the correct window.
     */
    public static void goToPreviousWindow() {
    	getSentinelDriver().goToPreviousWindow();
    }
    
	/**
	 * Asks the current driver to emulate clicking the browser's forward button.
	 */
	public static void navigateForward() {
		getSentinelDriver().navigateForward();
	}

	/**
	 * Asks the current driver to emulate clicking the browser's back button.
	 */
	public static void navigateBack() {
		getSentinelDriver().navigateBack();
	}

	/**
	 * Asks the current driver to emulate clicking the browser's refresh button.
	 */
	public static void refresh() {
		getSentinelDriver().refresh();
	}
	
    /**
     * Maximizes the browser window.
     */
    public static void maximizeWindow() {
    	getSentinelDriver().maximizeWindow();
    }
    
    /**
     * Returns the URL of the current page.
     * @return String current active window/tab's url
     */
    public static String getCurrentUrl() {
    	return getSentinelDriver().getWebDriver().getCurrentUrl();
    }
}