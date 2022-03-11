package com.dougnoel.sentinel.webdrivers;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.filemanagers.DownloadManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * This object factory is used to keep up with driver versions for all browsers.
 * For a list of supported browsers and operating systems, see the readme.
 */
public class WebDriverFactory {
	private static final Logger log = LogManager.getLogger(WebDriverFactory.class);
    private static WebDriver driver = null;
    private static WebDriverFactory instance = null;
    private static String parentHandle = null;

    private WebDriverFactory() {
        // Exists only to defeat instantiation.
    }
    
    /**
     * Creates and returns a useable WebDriver.
     * We use this factory method to handle keeping up with driver versions for all
     * browsers. The browser can be set in the config file or a system
     * variable. See the README for more information.
     * @return WebDriver An initialized <a href="https://www.seleniumhq.org/">Selenium
     *         WebDriver</a> object for the specified browser and operating system
     *         combination.
     */
    public static WebDriver instantiateWebDriver() {
        // Ensure we only have one instance of this class, so that we always return the
        // same driver.
        if (instance == null) {
            instance = new WebDriverFactory();
        }
        
        //Saucelabs Driver setup
        var saucelabsUserName = Configuration.toString("saucelabsUserName");
        if (saucelabsUserName != null) {
        	driver = SauceLabsDriverFactory.createSaucelabsDriver(); //NOTE: Returning the driver here so that we do not need an extra else statement but it must be set before being returned.
        	return driver;
        }
        
        var browser = Configuration.browser();
        
        //Grid Driver setup
        var gridUrl = Configuration.toString("gridUrl");
        if (gridUrl != null) {
        	driver = GridWebDriverFactory.createGridDriver(browser, gridUrl);
        	return driver;
        }

        // Initialize the driver object based on the browser and operating system (OS).
        // Throw an error if the value isn't found.   	
    	switch (browser) {
        case "chrome":
        	driver = createChromeDriver();
            break;
        case "edge":
        	WebDriverManager.edgedriver().setup();
        	driver = new EdgeDriver();
        	break;
        case "firefox":
    		WebDriverManager.firefoxdriver().setup();
    		driver = new FirefoxDriver();
            break;
        case "internetexplorer":
            var ieOptions = new InternetExplorerOptions();
        	ieOptions.ignoreZoomSettings();
        	WebDriverManager.iedriver().setup();
        	driver = new InternetExplorerDriver(ieOptions);
            break;
        case "opera":
        	WebDriverManager.operadriver().setup();
        	driver = new OperaDriver();
        	break;
        case "safari":
        	driver = new SafariDriver();
            break;
        default:
            throw new WebDriverException(SentinelStringUtils.format("Invalid browser type '{}' passed to WebDriverFactory. Could not resolve the reference. Check your spelling. Refer to the Javadoc for valid options.", browser));
        }
    	
    	parentHandle = driver.getWindowHandle();

        return driver;
    }

    /**
     * Returns the WebDriver instance. This will silently instatntiate the WebDriver if that has not been done yet.
     * 
     * @return WebDriver the created Selenium WebDriver
     */
    public static WebDriver getWebDriver() {
        if (instance == null || driver == null) {
        	instantiateWebDriver();
        }
        return driver;
    }
    
    /**
     * Closes the driver window if it is not the parent.
     */
    public static void close() {
    	if (parentHandle.contentEquals(driver.getWindowHandle())) {
        	getWebDriver().close();
        	driver = null;    		
    	}
    	else
    		getWebDriver().close();
    }
    
    /**
     * Quits the driver and sets the driver instance back to null.
     */
    public static void quit() {
    	if (exists()) {
    		getWebDriver().quit();
    		driver = null;
    	}
    	else {
    		log.info("Attempted to call quit on a driver that did not exist.");
    	}
    }
    
    public static boolean exists() {
    	return driver != null;
    }
    
    /**
     * Sets the download directory for chromedriver. Cannot be used with Saucelabs.
     * @param options ChromeOptions object to set
     */
    private static void setChromeDownloadDirectory(ChromeOptions options) {
        HashMap<String, Object> chromePrefs = new HashMap<>();
        //chromePrefs.put("download.prompt_for_download", false);
        chromePrefs.put("download.default_directory", DownloadManager.getDownloadDirectory());
        options.setExperimentalOption("prefs", chromePrefs);
    }
    
    /**
     * Creates a ChromeDriver. Makes it headless if the -Dheadless flag is set.
     * @return WebDriver ChromeDrvier
     */
    private static WebDriver createChromeDriver() {
    	var chromeOptions = new ChromeOptions();
    	setChromeDownloadDirectory(chromeOptions);
    	var headless = Configuration.toString("headless");
    	if (headless != null && !headless.equalsIgnoreCase("false")) {
    		chromeOptions.addArguments("--no-sandbox");
    		chromeOptions.addArguments("--disable-dev-shm-usage");
    		chromeOptions.addArguments("--headless");        		
    	}
    	var binary = Configuration.toString("chromeBrowserBinary");
    	if (binary != null)
    		chromeOptions.setBinary(binary);
    	WebDriverManager.chromedriver().setup();
    	return new ChromeDriver(chromeOptions);
    }

}