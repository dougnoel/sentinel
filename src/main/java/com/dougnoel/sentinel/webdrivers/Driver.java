package com.dougnoel.sentinel.webdrivers;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.pages.PageManager;

import io.appium.java_client.windows.WindowsDriver;

/**
 * Manages all drivers. Maintains as few drivers as possible for test execution.
 */
public class Driver {
	private static final Logger log = LogManager.getLogger(Driver.class);
	private static Map<String,WebDriver> drivers = new HashMap<>();
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
        	return drivers.computeIfAbsent(pageName, driver -> WinAppDriverFactory.createWinAppDriver());
    	}
        else {
    		pageName = "WebDriver"; //There can be only one.
    		return drivers.computeIfAbsent(pageName, driver -> WebDriverFactory.getWebDriver());
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
	private static WindowsDriver<WebElement> castWindowsDriver(WebDriver driver) {
    	return (WindowsDriver<WebElement>) driver;
    }

    /**
     * Quits all drivers and removes them from the list of active drivers.
     */
    public static void quit() {
    	drivers.forEach((pageObjectName, driver) -> {
    		log.debug("Closing {} driver: {}", pageObjectName, driver);
    		if (driver.getClass().getSimpleName().contentEquals("WindowsDriver"))
    			WinAppDriverFactory.quit(castWindowsDriver(driver));
    		else {
    			WebDriverFactory.quit();
    		}
    	});
    	drivers.clear();
    }
}