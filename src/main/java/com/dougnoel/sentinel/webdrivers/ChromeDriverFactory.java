package com.dougnoel.sentinel.webdrivers;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.WebDriverException;
import com.dougnoel.sentinel.exceptions.WebDriverNotExecutableException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

public class ChromeDriverFactory {
	private static final Logger log = LogManager.getLogger(ChromeDriverFactory.class); // Create a logger.
	
	private ChromeDriverFactory() {
		// Exists to defeat instantiation.
	}
	
    /**
     * Creates a Chrome WebDriver and returns it.
     * @return WebDriver as a Chrome WebDriver object
     */
    protected static WebDriver createChromeDriver() {
    	setChromeDriverPath();
        setChromeDownloadDirectory("downloads");
        try {
        	return new ChromeDriver();
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
     * Creates a Headless Chrome WebDriver and returns it.
     * @return WebDriver as a Headless Chrome WebDriver object
     */
    protected static WebDriver createHeadlessChromeDriver() {
    	setChromeDriverPath();
        setChromeDownloadDirectory("downloads");
        try {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--headless");
            return new ChromeDriver(options);
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
    private static void setChromeDriverPath() {
    	String driverPath = WebDriverFactory.getDriverPath();
    	if (driverPath == null)
    	{
	        switch (Configuration.operatingSystem()) {
	        case "linux":
	            driverPath = "src/main/resources/drivers/linux/chromedriver";
	            break;
	        case "mac":
	            driverPath = "src/main/resources/drivers/mac/chromedriver";
	            break;
	        case "windows":
	            driverPath = "src\\main\\resources\\drivers\\windows\\chromedriver.exe";
	            break;
	        default:
	            throw new WebDriverException(WebDriverFactory.getMissingOSConfigurationErrorMessage());
	        }
    	}
        System.setProperty("webdriver.chrome.driver", driverPath);
    }
    
    /**
     * Sets the download directory for chromedriver. Cannot be used with Saucelabs.
     * @param filePath String path to the download directory
     */
    private static void setChromeDownloadDirectory(String filePath) {
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("download.default_directory", filePath);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
    }
}
