package com.dougnoel.sentinel.webdrivers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.exceptions.WebDriverException;
import com.dougnoel.sentinel.exceptions.WebDriverNotExecutableException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

public class FirefoxDriverFactory {
	private static final Logger log = LogManager.getLogger(FirefoxDriverFactory.class); // Create a logger.
	
	private FirefoxDriverFactory() {
		// Exists to defeat instantiation.
	}
	
    /**
     * Creates a Firefox WebDriver and returns it.
     * @return WebDriver a Firefox WebDriver object
     */
    protected static WebDriver createFirefoxDriver() {
    	setFirefoxDriverPath();
        try {
        	return new FirefoxDriver();
        }
		catch (IllegalStateException e) {
			String errorMessage = SentinelStringUtils.format(WebDriverFactory.DRIVERNOTFOUNDERRORMESSAGEPATTERN, e.getMessage());
			log.error(errorMessage);
			throw new WebDriverNotExecutableException(errorMessage, e);
		}
        catch (org.openqa.selenium.WebDriverException e) {
        	log.error(e.getMessage());
        	throw new WebDriverException(e);
        }
    }
    
    /**
     * Sets the path for the ChromeDriver based on operating system. Uses a custom driver if it is set as a configuration.
     */
    private static void setFirefoxDriverPath() {
    	String driverPath = WebDriverFactory.getDriverPath();
    	if (driverPath == null)
    	{
	        switch (ConfigurationManager.getOperatingSystem()) {
	        case "linux":
	            driverPath = "src/main/resources/drivers/linux/geckodriver";
	            break;
	        case "mac":
	            driverPath = "src/main/resources/drivers/mac/geckodriver";
	            break;
	        case "windows":
	            driverPath = "src\\main\\resources\\drivers\\windows\\geckodriver.exe";
	            break;
	        default:
	            throw new WebDriverException(WebDriverFactory.getMissingOSConfigurationErrorMessage());
	        }
    	}
        System.setProperty("webdriver.gecko.driver", driverPath);
    }

}
