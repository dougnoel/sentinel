package com.dougnoel.sentinel.webdrivers;

import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.WebDriverException;
import com.dougnoel.sentinel.filemanagers.DownloadManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import io.github.bonigarcia.wdm.WebDriverManager;

/**
 * This object factory is used to keep up with driver versions for all browsers.
 * For a list of supported browsers and operating systems, see the readme.
 */
public class WebDriverFactory {
    private static WebDriver driver = null;
    private static WebDriverFactory instance = null;

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

        String browser = Configuration.browser();

        // Initialize the driver object based on the browser and operating system (OS).
        // Throw an error if the value isn't found.   	
    	switch (browser) {
        case "chrome":
        	var chromeOptions = new ChromeOptions();
        	setChromeDownloadDirectory(chromeOptions);
        	var headless = Configuration.toString("headless");
        	if (headless != null && !headless.equalsIgnoreCase("false")) {
        		chromeOptions.addArguments("--no-sandbox");
        		chromeOptions.addArguments("--disable-dev-shm-usage");
        		chromeOptions.addArguments("--headless");        		
        	}
        	WebDriverManager.chromedriver().setup();
        	driver = new ChromeDriver(chromeOptions);
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

        return driver;
    }

    /**
     * Returns the WebDriver instance. This will silently instatntiate the WebDriver if that has not been done yet.
     * 
     * @return WebDriver the created Selenium WebDriver
     */
    public static WebDriver getWebDriver()  {
        if (instance == null) {
        	instantiateWebDriver();
        }
        return driver;
    }
    
    /**
     * Sets the download directory for chromedriver. Cannot be used with Saucelabs.
     * @param options ChromeOptions object to set
     */
    private static void setChromeDownloadDirectory(ChromeOptions options) {
        // Set a Download Directory if one was specified on the command line
        var downloadDirectory = Configuration.toString("download");
        if (downloadDirectory != null)
            DownloadManager.setDownloadDirectory(downloadDirectory);
        else
        	DownloadManager.setDownloadDirectory("downloads");
        
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("download.default_directory", DownloadManager.getDownloadDirectory());
        options.setExperimentalOption("prefs", chromePrefs);
    }

}