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
     * Creates a single threaded Saucelabs WebDriver using the os, browser, and browser version passed to it.
     * @param operatingSystem String the operating system you want Saucelabs to stand up
     * @param browser String the browser you want Saucelabs to use
     * @param browserVersion String the version of the browser you want Saucelabs to use
     * @return WebDriver
     * @throws ConfigurationNotFoundException if a requested configuration property has not been set
     */
    private static WebDriver createSaucelabsDriver(String operatingSystem, String browser, String browserVersion) throws ConfigurationNotFoundException {
        URL SAUCELABS_URL;
		try {
			SAUCELABS_URL = new URL("https://ondemand.saucelabs.com:443/wd/hub");
		} catch (java.net.MalformedURLException e) {
			throw new MalformedURLException(e);
		}
        
        MutableCapabilities options = new MutableCapabilities();
        options.setCapability("platform", operatingSystem);
        options.setCapability("browserName", browser);
        if (browserVersion != null) {
        	options.setCapability("version", browserVersion);
        } else {
        	options.setCapability("version", "latest");
        }
        
        options.setCapability("username", ConfigurationManager.getProperty("saucelabsUserName"));
        options.setCapability("accesskey", ConfigurationManager.getProperty("saucelabsAccessKey"));
        
        String parentTunnel = ConfigurationManager.getOptionalProperty("parenttunnel");
        String tunnelIdentifier = ConfigurationManager.getOptionalProperty("tunnelIdentifier");
        
        if (StringUtils.isNotEmpty(parentTunnel)) {
        	options.setCapability("parent-tunnel", parentTunnel);
        }
        
        if (StringUtils.isNotEmpty(tunnelIdentifier)) {
        	options.setCapability("tunnelIdentifier", tunnelIdentifier);
        }        
        
        RemoteWebDriver driver = new RemoteWebDriver(SAUCELABS_URL, options);
        
        return driver;
    }
    
    //TODO: Add all of the valid browser options and make them match saucelabs options
    /**
     * Creates and returns a useable WebDriver.
     * We use this factory method to handle keeping up with driver versions for all
     * browsers. All the browser drivers can be found in the root of the project
     * under the drivers/[os]/ paths.
     * <p>
     * All Strings are converted to lower case and spaces are removed, so strings
     * like "FireFox", "chrome" and "Internet Explorer" are all valid.
     * 
     * @param browser
     *            String Valid options: chrome, firefox, internet explorer, safari
     * @param operatingSystem
     *            String Valid options: linux, macintosh (mac, os x), windows (win)
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
    	
        String operatingSystem = ConfigurationManager.getProperty("os");
        String browser = ConfigurationManager.getProperty("browser");
        
        //Driver setup
        String saucelabsUserName = ConfigurationManager.getOptionalProperty("saucelabsUserName");
        if (saucelabsUserName != null) {
        	String browserVersion = ConfigurationManager.getOptionalProperty("browserVersion"); // If we do not specify a browser version, we use the latest
        	driver = createSaucelabsDriver(operatingSystem, browser, browserVersion);
        } else {
            // Declare a variable to store the filePath of the driver
            String driverPath;

            // Set a Download Directory if one was specified on the command line
            String downloadDirectory = ConfigurationManager.getOptionalProperty("download");
            if (downloadDirectory != null)
                DownloadManager.setDownloadDirectory(downloadDirectory);

            // Make sure whatever string we are passed is all lower case and all spaces are
            // removed.
            browser = browser.replaceAll("\\s+", "").toLowerCase();
            if (browser.equals("ie"))
                browser = "internetexplorer";

            operatingSystem = operatingSystem.replaceAll("\\s+", "").toLowerCase();
            if (operatingSystem.equals("macintosh") || operatingSystem.equals("osx"))
                operatingSystem = "mac";
            else if (operatingSystem.equals("win"))
                operatingSystem = "windows";

            try {
                // Initialize the driver object based on the browser and operating system (OS).
                // Throw an error if the value isn't found.
            	String missingOSConfigurationErrorMessage = StringUtils.format("Invalid operating system '{}' passed to WebDriverFactory. Could not resolve the reference. Check your spelling. Refer to the Javadoc for valid options.", operatingSystem);
                String osNotCompatibleWithGivenBrowserErrorMessage = StringUtils.format("Invalid operating system '{}' passed to WebDriverFactory. {} can only be used with {}. Refer to the Javadoc for valid options.", operatingSystem, browser, operatingSystem);
            	
            	switch (browser) {
                case "chrome":
                    switch (operatingSystem) {
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
                        throw new WebDriverException(missingOSConfigurationErrorMessage);
                    }
                    System.setProperty("webdriver.chrome.driver", driverPath);
                    setChromeDownloadDirectory("downloads");
                    try {
                    	driver = new ChromeDriver();
                    }
            		catch (IllegalStateException e) {
            			String errorMeessage = "The driver does not have execute permissions. On linux/mac run chmod +x on the driver.";
            			throw new WebDriverNotExecutableException(errorMeessage, e);
            		}
                    break;
                case "firefox":
                    switch (operatingSystem) {
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
                        throw new WebDriverException(missingOSConfigurationErrorMessage);
                    }
                    System.setProperty("webdriver.gecko.driver", driverPath);
                    driver = new FirefoxDriver();
                    break;
                case "internetexplorer":
                    switch (operatingSystem) {
                    case "linux":
                    case "mac":
                        throw new WebDriverException(osNotCompatibleWithGivenBrowserErrorMessage);
                    case "windows":
                        driverPath = "src\\main\\resources\\drivers\\windows\\IEDriverServer.exe";
                        break;
                    default:
                        throw new WebDriverException(missingOSConfigurationErrorMessage);
                    }
                    System.setProperty("webdriver.ie.driver", driverPath);
                	InternetExplorerOptions options = new InternetExplorerOptions();
                	options.ignoreZoomSettings();
//                	options.requireWindowFocus();
                    driver = new InternetExplorerDriver(options);
                    break;
                case "safari":
                    switch (operatingSystem) {
                    case "linux":
                    case "windows":
                        throw new WebDriverException(osNotCompatibleWithGivenBrowserErrorMessage);
                    case "mac":
                        // Nothing to do here, as Apple has already set this up on macs.
                        break;
                    default:
                        throw new WebDriverException(missingOSConfigurationErrorMessage);
                    }
                    driver = new SafariDriver();
                    break;
                default:
                    throw new WebDriverException(StringUtils.format("Invalid browser type '{}' passed to WebDriverFactory. Could not resolve the reference. Check your spelling. Refer to the Javadoc for valid options.", browser));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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

}