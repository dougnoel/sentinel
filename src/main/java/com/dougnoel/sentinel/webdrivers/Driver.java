package com.dougnoel.sentinel.webdrivers;

import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.pages.PageManager;

/**
 * Manages all drivers. Maintains as few drivers as possible for test execution.
 */
public class Driver {
	private static final Logger log = LogManager.getLogger(Driver.class);
	protected Map<String,WebDriver> drivers;

    private Driver() {
        // Exists only to defeat instantiation.
    }

    /**
     * Returns the WebDriver instance. This will silently instatntiate the WebDriver if that has not been done yet.
     * 
     * @return WebDriver the created Selenium WebDriver
     */
    public static WebDriver getDriver() {
    	//TODO: Implement the ability to store the driver if it is created for an executable and check for it using the page name
    	//TODO: Store the Webdriver for all web tests once
    	//TODO: Get rid of the need to call Instantiate Web driver at the beginning of tests so we can create all drivers on demand
    	//TODO: Turn on WinAppDriver on the command line if its not on
    	if (PageManager.getPage().getPageObjectType() == PageObjectType.EXECUTABLE)
        	return WinAppDriverFactory.createWinAppDriver();
        else
        	return WebDriverFactory.getWebDriver();
    }

    
}