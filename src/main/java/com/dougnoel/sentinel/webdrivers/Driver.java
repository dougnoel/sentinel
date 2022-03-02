package com.dougnoel.sentinel.webdrivers;

import java.util.HashMap;
import java.util.Map;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.framework.PageManager;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;

/**
 * Manages all drivers. Maintains as few drivers as possible for test execution.
 */
public class Driver {
	private static final Logger log = LogManager.getLogger(Driver.class);
	//Map each page object name to a driver, which may be the same driver
	private static Map<String,WebDriver> drivers = new HashMap<>();
	private static WebDriver currentDriver = null;
	
	private Driver() {
        // Exists only to defeat instantiation.
    }

    /**
     * Returns the WebDriver instance. This will silently instantiate the WebDriver if that has not been done yet.
     * 
     * @return WebDriver the created Selenium WebDriver
     */
    public static WebDriver getDriver() {
    	String pageName = PageManager.getPage().getName();
    	if (PageManager.getPage().getPageObjectType() == PageObjectType.EXECUTABLE) {
    		if (Configuration.hasExecutables(pageName)) {
    			currentDriver = drivers.computeIfAbsent(pageName, driver -> WinAppDriverFactory.createWinAppDriver());
    		}
    		drivers.putIfAbsent(pageName, currentDriver);
    	}
        else {
    		pageName = "WebDriver"; //There can be only one.
    		currentDriver = drivers.computeIfAbsent(pageName, driver -> WebDriverFactory.getWebDriver());
        }
    	return currentDriver;
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

    /**
     * Quits all drivers and removes them from the list of active drivers.
     */
    public static void quitAll() {
    	drivers.forEach((pageObjectName, driver) -> {
    		log.debug("Closing {} driver: {}", pageObjectName, driver);
    		if (driver.getClass().getSimpleName().contentEquals("WindowsDriver"))
    			WinAppDriverFactory.quit(castWindowsDriver(driver));
    		else {
    			WebDriverFactory.quit();
    		}
    	});
    	drivers.clear();
    	currentDriver = null;
    }
    
    /**
     * Quit the current driver and remove all references of it.
     */
    public static void quit() {
    	//Search the map for the driver and delete all references to it
    	
    }
    
    /**
     * Closes the current window and moves the driver to the previous window. If no previous window exists,
     * we call close to clean up.
     */
    public static void close() {
    	//close the current SentinelDriver window which calls the WindowList
    }
    
    public static void goToNextWindow() {
    	
    }
    
    public static void goToPreviousWindow() {
    	
    }
    
	/**
	 * Emulate clicking the browser's forward button.
	 */
	public static void navigateForward() {
		currentDriver.navigate().forward();
	}

	/**
	 * Emulate clicking the browser's back button.
	 */
	public static void navigateBack() {
		currentDriver.navigate().back();
	}

	/**
	 * Emulate clicking the browser's refresh button.
	 */
	public static void refresh() {
		currentDriver.navigate().refresh();
	}
}