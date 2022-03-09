package com.dougnoel.sentinel.webdrivers;

import java.util.EnumMap;
import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.framework.PageManager;

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
     * @return WebDriver the created Selenium WebDriver
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
    
    public static void goToNextWindow() {
    	
    }
    
    public static void goToPreviousWindow() {
    	
    }
    
	/**
	 * Emulate clicking the browser's forward button.
	 */
	public static void navigateForward() {
		getSentinelDriver().navigateForward();
	}

	/**
	 * Emulate clicking the browser's back button.
	 */
	public static void navigateBack() {
		getSentinelDriver().navigateBack();
	}

	/**
	 * Emulate clicking the browser's refresh button.
	 */
	public static void refresh() {
		getSentinelDriver().refresh();
	}
}