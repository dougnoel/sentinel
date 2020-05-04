package com.dougnoel.sentinel.webdrivers;

import java.net.URL;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.exceptions.ConfigurationNotFoundException;
import com.dougnoel.sentinel.exceptions.WebDriverNotExecutableException;
import com.dougnoel.sentinel.exceptions.MalformedURLException;
import com.dougnoel.sentinel.exceptions.WebDriverException;
import com.dougnoel.sentinel.filemanagers.DownloadManager;
import com.dougnoel.sentinel.strings.StringUtils;

/**
 * This object factory is used to keep up with driver versions for all browsers.
 * All the browser drivers can be found in the root of the project under the
 * drivers/[os]/ paths.
 * <p>
 * The purpose of this class is to use the
 * <a href="https://en.wikipedia.org/wiki/Factory_method_pattern">Factory Design
 * Pattern</a> to encapsulate the creation of drivers for various browsers and
 * share that functionality across projects. All drivers are stored as part of
 * the project and updated from one central repository. Pulling the latest code
 * from git will ensure you have the latest browser drivers and that you do not
 * need to spend time keeping them up to date. All drivers are assumed to be
 * 64-bit. Chromedriver and Internet Explorer drivers for Windows are 32-bit.
 * <p>
 * <b>Supported Browsers:</b>
 * <ul>
 * <li>Chrome
 * (<a href="http://chromedriver.chromium.org/downloads">Chromedriver</a>
 * 2.3.8)</li>
 * <li>Firefox
 * (<a href="https://github.com/mozilla/geckodriver/releases">Geckodriver</a>
 * 0.20.1)</li>
 * <li>Internet Explorer
 * (<a href="https://www.seleniumhq.org/download/">Internet Explorer Driver
 * Server</a> 3.12.0)</li>
 * <li>Safari (Safaridriver 10) (Comes installed on OS X)
 * </ul>
 * <p>
 * <b>Supported Operating Systems:</b>
 * <ul>
 * <li>Linux (Chrome/Firefox)</li>
 * <li>OS X (Chrome/Firefox/Safari)</li>
 * <li>Windows (Chrome/Firefox/Internet Explorer</li>
 * </ul>
 */
public class WebDriverFactory {
    private static final Logger log = LogManager.getLogger(WebDriverFactory.class); // Create a logger.
    private static WebDriver driver = null;

    private static WebDriverFactory instance = null;

    private static String downloadPath = "../downloads";

    protected WebDriverFactory() {
        // Exists only to defeat instantiation.
    }
    
    /**
     * Creates a single threaded Saucelabs WebDriver.
     * @return WebDriver a Saucelabs WebDriver
     * @throws ConfigurationNotFoundException if a requested configuration property has not been set
     */
    private static WebDriver createSaucelabsDriver() throws ConfigurationNotFoundException {
        URL SAUCELABS_URL;
		try {
			SAUCELABS_URL = new URL("https://ondemand.saucelabs.com:443/wd/hub");
		} catch (java.net.MalformedURLException e) {
			throw new MalformedURLException(e);
		}
        
		String browser = ConfigurationManager.getProperty("browser");
		String operatingSystem = ConfigurationManager.getProperty("os");
		
        MutableCapabilities options = new MutableCapabilities();
        options.setCapability("platform", operatingSystem);
        options.setCapability("browserName", browser);
        String browserVersion = ConfigurationManager.getOptionalProperty("browserVersion");
        if (browserVersion != null) {
        	options.setCapability("version", browserVersion);
        } else {
        	options.setCapability("version", "latest");
        }
        
        options.setCapability("username", ConfigurationManager.getProperty("saucelabsUserName"));
        options.setCapability("accesskey", ConfigurationManager.getProperty("saucelabsAccessKey"));
        
        options = setSaucelabsTestNameProperty(options);
        
        options = setOptionalSaucelabsProperty("parent-tunnel", options);
        options = setOptionalSaucelabsProperty("tunnelIdentifier", options);
        options = setOptionalSaucelabsProperty("tags", options);
        options = setOptionalSaucelabsProperty("build", options);
        
        RemoteWebDriver driver = new RemoteWebDriver(SAUCELABS_URL, options);
        
        return driver;
    }
    
    /**
     * Looks for a passed Saucelabs property name and sets it if it exists.
     * @param saucelabsPropertyName String the property to be set if it exists
     * @param options MutableCapabilities the MutableCapabilities object in which to set the properties
     * @return MutableCapabilities the new MutableCapabilities object with the properties set.
     */
    private static MutableCapabilities setOptionalSaucelabsProperty(String saucelabsPropertyName, MutableCapabilities options) {
        String saucelabsProperty = ConfigurationManager.getOptionalProperty(saucelabsPropertyName);
        
        if (StringUtils.isNotEmpty(saucelabsProperty)) {
        	options.setCapability(saucelabsPropertyName, saucelabsProperty);
        }
        
    	return options;
    }
    
    /**
     * Pulls config values and sets a string to pass to Saucelabs for the job.
     * @param options MutableCapabilities object to modify
     * @return MutableCapabilities modified MutableCapabilities object
     */
    private static MutableCapabilities setSaucelabsTestNameProperty(MutableCapabilities options)
    {
        String testName = "";
        String jobName = ConfigurationManager.getOptionalProperty("name");
        if (jobName != null) {
        	testName += "Name: " + jobName + " ";
        }
        String tags = ConfigurationManager.getOptionalProperty("tags");
        if (tags != null) {
        	testName += "Tags: " + tags + " ";
        }
        String userName = System.getProperty("user.name");
        if (userName != null) {
        	testName += "User: " + userName + " ";
        }
        String build = ConfigurationManager.getOptionalProperty("build");
        if (build != null) {
        	testName += "Build: " + build + " ";
        }
        if (testName.isEmpty()) {
        	testName += "Default Sentinel Test Name";
        }
        else {
        	testName = testName.stripTrailing();
        }
        
        options.setCapability("name", testName);
        return options;
    }
    
    //TODO: Add all of the valid browser options and make them match saucelabs options
    /**
     * Creates and returns a useable WebDriver.
     * We use this factory method to handle keeping up with driver versions for all
     * browsers. All the browser drivers can be found in the root of the project
     * under the drivers/[os]/ paths. The browser can be set in the config file or a system
     * variable. See the README for more information.
     * @return WebDriver An initialized <a href="https://www.seleniumhq.org/">Selenium
     *         WebDriver</a> object for the specified browser and operating system
     *         combination.
     * @throws MalformedURLException if the saucelabs URL is malformed
     * @throws WebDriverException if error thrown while creating WebDriver instance
     * @throws ConfigurationNotFoundException if a needed configuration value cannot be found
     */
    public static WebDriver instantiateWebDriver() throws WebDriverException, MalformedURLException, ConfigurationNotFoundException {
        // Ensure we only have one instance of this class, so that we always return the
        // same driver.
        if (instance == null) {
            instance = new WebDriverFactory();
        }
        
        //Saucelabs Driver setup
        String saucelabsUserName = ConfigurationManager.getOptionalProperty("saucelabsUserName");
        if (saucelabsUserName != null) {
        	return driver = createSaucelabsDriver(); //NOTE: Returning the driver here so that we do not need an extra else statement.
        }

        // Set a Download Directory if one was specified on the command line
        String downloadDirectory = ConfigurationManager.getOptionalProperty("download");
        if (downloadDirectory != null)
            DownloadManager.setDownloadDirectory(downloadDirectory);

        String browser = getBrowserName();

        // Initialize the driver object based on the browser and operating system (OS).
        // Throw an error if the value isn't found.   	
    	switch (browser) {
        case "chrome":
        	driver = createChromeDriver();
            break;
        case "firefox":
        	driver = createFirefoxDriver();
            break;
        case "internetexplorer":
        	driver = createInternetExplorerDriver();
            break;
        case "safari":
        	driver = createSafariDriver();
            break;
        default:
            throw new WebDriverException(StringUtils.format("Invalid browser type '{}' passed to WebDriverFactory. Could not resolve the reference. Check your spelling. Refer to the Javadoc for valid options.", browser));
        }

        return driver;
    }

    /**
     * Returns the WebDriver instance. This will silently log an error if the WebDriver has not been instantiated yet.
     * 
     * @return WebDriver the created Selenium WebDriver
     */
    public static WebDriver getWebDriver()  {
        if (instance == null) {
        	String errorMessage = "WebDriver has not been created. Call WebDriver.instantiateWebDriver() before calling WebDriver.getWebDriver";
        	log.error(errorMessage);
        }
        return driver;
    }

    /**
     * Sets the download directory for chromedriver. Cannot be used with Saucelabs.
     * @param filePath String path to the download directory
     */
    private static void setChromeDownloadDirectory(String filePath) {
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        chromePrefs.put("download.default_directory", filePath);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
    }

    /**
     * Returns the download path
     * 
     * @return downloadPath String the file path to the download directory
     */
    public static String getDownloadPath() {
        return downloadPath;
    }

    /**
     * Sets the download path for the WebDriver instance
     * TODO: Make sure this works for all browser types
     * TODO: Figure out if we can pass the value to Saucelabs
     * 
     * @param downloadPath the path for all webdriver downloads
     */
    public static void setDownloadPath(String downloadPath) {
        WebDriverFactory.downloadPath = downloadPath;
    }
    
    /**
     * Returns a sanitized version of the operating system set in the config file or on the command line.
     * @return String a sanitized string containing the operating system
     * @throws ConfigurationNotFoundException if the configuration data cannot be read
     */
    private static String getOperatingSystem() throws ConfigurationNotFoundException {
    	//TODO: Add auto detection
    	//TODO Make this useable by Saucelabs driver
    	String operatingSystem = ConfigurationManager.getProperty("os");
        operatingSystem = operatingSystem.replaceAll("\\s+", "").toLowerCase();
        if (operatingSystem.equals("macintosh") || operatingSystem.equals("osx"))
            operatingSystem = "mac";
        else if (operatingSystem.equals("win"))
            operatingSystem = "windows";
        
        return operatingSystem;
    }
    
    /**
     * Returns an error message string 
     * @return String error message
     * @throws ConfigurationNotFoundException if the configuration data cannot be read
     */
    private static String getMissingOSConfigurationErrorMessage() throws ConfigurationNotFoundException {
    	String operatingSystem = ConfigurationManager.getProperty("os");
    	return StringUtils.format("Invalid operating system '{}' passed to WebDriverFactory. Could not resolve the reference. Check your spelling. Refer to the Javadocs for valid options.", operatingSystem);
        
    }
    
    /**
     * Returns an error message string 
     * @return String error message
     * @throws ConfigurationNotFoundException if the configuration data cannot be read
     */
    private static String getOSNotCompatibleWithBrowserErrorMessage() throws ConfigurationNotFoundException {
    	String operatingSystem = ConfigurationManager.getProperty("os");
    	String browser = ConfigurationManager.getProperty("browser");
    	return StringUtils.format("Invalid operating system '{}' passed to WebDriverFactory for the {} driver. Refer to the Javadocs for valid options.", operatingSystem, browser);
    	
    }
    
    /**
     * Returns a sanitized version of the browser set in the config file or on the command line.
     * @return String a sanitized string containing the browser
     * @throws ConfigurationNotFoundException if the configuration data cannot be read
     */
    private static String getBrowserName() throws ConfigurationNotFoundException {
    	//TODO: Add auto detection
    	//TODO Make this useable by Saucelabs driver
    	String browser = ConfigurationManager.getProperty("browser");
        // Make sure whatever string we are passed is all lower case and all spaces are
        // removed.
        browser = browser.replaceAll("\\s+", "").toLowerCase();
        if (browser.equals("ie"))
            browser = "internetexplorer";
        return browser;
    }
    
    /**
     * Creates a Chrome WebDriver and returns it.
     * @return WebDriver a Chrome WebDriver object
     * @throws WebDriverException if the WebDriver creation fails
     * @throws ConfigurationNotFoundException if the configuration data cannot be read
     */
    private static WebDriver createChromeDriver() throws WebDriverException, ConfigurationNotFoundException {
    	String driverPath;
        switch (getOperatingSystem()) {
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
            throw new WebDriverException(getMissingOSConfigurationErrorMessage());
        }
        System.setProperty("webdriver.chrome.driver", driverPath);
        setChromeDownloadDirectory("downloads");
        try {
        	return new ChromeDriver();
        }
		catch (IllegalStateException e) {
			String errorMeessage = "The driver does not have execute permissions or cannot be found. Make sure it is in the correct location. On linux/mac run chmod +x on the driver.";
			throw new WebDriverNotExecutableException(errorMeessage, e);
		}
    }
    
    /**
     * Creates a Firefox WebDriver and returns it.
     * @return WebDriver a Firefox WebDriver object
     * @throws WebDriverException if the WebDriver creation fails
     * @throws ConfigurationNotFoundException if the configuration data cannot be read
     */
    private static WebDriver createFirefoxDriver() throws WebDriverException, ConfigurationNotFoundException {
    	String driverPath;
        switch (getOperatingSystem()) {
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
            throw new WebDriverException(getMissingOSConfigurationErrorMessage());
        }
        System.setProperty("webdriver.gecko.driver", driverPath);
        return new FirefoxDriver();
    }
    
    /**
     * Creates an IE WebDriver and returns it.
     * @return WebDriver an IE WebDriver object
     * @throws WebDriverException if the WebDriver creation fails
     * @throws ConfigurationNotFoundException if the configuration data cannot be read
     */
    private static WebDriver createInternetExplorerDriver() throws WebDriverException, ConfigurationNotFoundException {
    	String driverPath;
        switch (getOperatingSystem()) {
        case "linux":
        case "mac":
            throw new WebDriverException(getOSNotCompatibleWithBrowserErrorMessage());
        case "windows":
            driverPath = "src\\main\\resources\\drivers\\windows\\IEDriverServer.exe";
            break;
        default:
            throw new WebDriverException(getMissingOSConfigurationErrorMessage());
        }
        System.setProperty("webdriver.ie.driver", driverPath);
    	InternetExplorerOptions options = new InternetExplorerOptions();
    	options.ignoreZoomSettings();
        return new InternetExplorerDriver(options);
    }

    /**
     * Creates a Safari WebDriver and returns it.
     * @return WebDriver a Safari WebDriver object
     * @throws WebDriverException if the WebDriver creation fails
     * @throws ConfigurationNotFoundException if the configuration data cannot be read
     */
    private static WebDriver createSafariDriver() throws WebDriverException, ConfigurationNotFoundException {
        switch (getOperatingSystem()) {
        case "linux":
        case "windows":
            throw new WebDriverException(getOSNotCompatibleWithBrowserErrorMessage());
        case "mac":
            // Nothing to do here, as Apple has already set this up on macs.
            break;
        default:
            throw new WebDriverException(getMissingOSConfigurationErrorMessage());
        }
        return new SafariDriver();
    }
}