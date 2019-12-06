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
import com.dougnoel.sentinel.exceptions.ConfigurationMappingException;
import com.dougnoel.sentinel.exceptions.ConfigurationParseException;
import com.dougnoel.sentinel.exceptions.FileNotFoundException;
import com.dougnoel.sentinel.exceptions.IOException;
import com.dougnoel.sentinel.exceptions.MalformedURLException;
import com.dougnoel.sentinel.exceptions.MissingConfigurationException;
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
     * Creates and Returns a usable WebDriver instance. 
     * 
     * @return WebDriver
     * @throws MissingConfigurationException if the requested configuration property has not been set
     * @throws ConfigurationParseException if error thrown while reading configuration file into sentinel
     * @throws ConfigurationMappingException if error thrown while mapping configuration file to sentinel
     * @throws IOException if other error occurs when mapping yml file into sentinel
     * @throws WebDriverException if error thrown while creating WebDriver instance
     * @throws FileNotFoundException if the sentinel configuration file does not exist.
     */
    public static WebDriver instantiateWebDriver() throws MissingConfigurationException, ConfigurationParseException, ConfigurationMappingException, IOException, WebDriverException, FileNotFoundException {
    	String browser = ConfigurationManager.getOptionalProperty("browser");
        String operatingSystem = ConfigurationManager.getOptionalProperty("os");
        String saucelabsUserNameAndKey = ConfigurationManager.getOptionalProperty("saucelabs");
        
        if (saucelabsUserNameAndKey == null) {
            if (browser == null) {
                throw new MissingConfigurationException(StringUtils.format("Browser system property set as {}. Browser property must be set in sentinel.yml or via the command line. See project README for details.", browser));
            }
            if (operatingSystem == null) {
                throw new MissingConfigurationException(StringUtils.format("OS system property set as {}. OS property must be set in sentinel.yml or via the command line. See project README for details.", operatingSystem));
            }
        }
			return instantiateWebDriver(browser, operatingSystem);
    }

    /**
     * Creates a single threaded Saucelabs WebDriver using the os, browser, and browser version set in the 
     * configuration file or passed in on the command line.
     * @return WebDriver
     * @throws ConfigurationParseException if error thrown while reading configuration file into sentinel
     * @throws ConfigurationMappingException if error thrown while mapping configuration file to sentinel
     * @throws IOException if other error occurs when mapping yml file into sentinel
     * @throws MissingConfigurationException if the requested configuration property has not been set
     * @throws FileNotFoundException if the sentinel configuration file does not exist
     */
    private static WebDriver createSaucelabsDriver() throws ConfigurationParseException, ConfigurationMappingException, IOException, MissingConfigurationException, FileNotFoundException {
        String operatingSystem = ConfigurationManager.getOptionalProperty("os");
        String browserVersion = ConfigurationManager.getOptionalProperty("browserVersion");
        String browser = ConfigurationManager.getOptionalProperty("browser");
        
    	return createSaucelabsDriver(operatingSystem, browser, browserVersion);
    }
    
    /**
     * Creates a single threaded Saucelabs WebDriver using the os, browser, and browser version passed to it.
     * @return WebDriver
     * @throws ConfigurationParseException if error thrown while reading configuration file into sentinel
     * @throws ConfigurationMappingException if error thrown while mapping configuration file to sentinel
     * @throws IOException if other error occurs when mapping yml file into sentinel
     * @throws MissingConfigurationException if the requested configuration property has not been set
     * @throws FileNotFoundException if the sentinel configuration file does not exist
     */
    private static WebDriver createSaucelabsDriver(String operatingSystem, String browser, String browserVersion) throws ConfigurationParseException, ConfigurationMappingException, IOException, MissingConfigurationException, FileNotFoundException {
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
        
        options.setCapability("username", ConfigurationManager.getOptionalProperty("saucelabsUserName"));
        options.setCapability("accesskey", ConfigurationManager.getOptionalProperty("saucelabsAccessKey"));
        
        String parentTunnel = ConfigurationManager.getOptionalProperty("parenttunnel");
        String tunnelIdentifier = ConfigurationManager.getOptionalProperty("tunnelIdentifier");
        
        if (parentTunnel != null) {
        	options.setCapability("parent-tunnel", parentTunnel);
        }
        
        if (tunnelIdentifier != null) {
        	options.setCapability("tunnelIdentifier", tunnelIdentifier);
        }        
        
        RemoteWebDriver driver = new RemoteWebDriver(SAUCELABS_URL, options);
        
        return driver;
    }
    
    /**
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
     * @throws MissingConfigurationException if the requested configuration property has not been set
     * @throws ConfigurationParseException if error thrown while reading configuration file into sentinel
     * @throws ConfigurationMappingException if error thrown while mapping configuration file to sentinel
     * @throws IOException if other error occurs when mapping yml file into sentinel
     * @throws WebDriverException if error thrown while creating WebDriver instance
     * @throws FileNotFoundException if the sentinel configuration file does not exist.
     */
    public static WebDriver instantiateWebDriver(String browser, String operatingSystem) throws WebDriverException, MalformedURLException, ConfigurationMappingException,
    ConfigurationParseException, IOException, MissingConfigurationException, FileNotFoundException {
        // Ensure we only have one instance of this class, so that we always return the
        // same driver.
        if (instance == null) {
            instance = new WebDriverFactory();
        }
        
        //Driver setup
        String saucelabsUserName = ConfigurationManager.getOptionalProperty("saucelabsUserName");
        if (saucelabsUserName != null) {
        	driver = createSaucelabsDriver();
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
                    driver = new ChromeDriver();
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
     * Wraps WebDriverFactory#getWebDriver() and handles errors
     * 
     * @return WebDriver
     */
    public static WebDriver getWebDriverAndHandleErrors() {
    	String errorMessage = "Retrieval WebDriver instance failed with a '{}', {}"; 
    	try {
    		getWebDriver();
    	} catch (MissingConfigurationException e) {
    		log.error(StringUtils.format(errorMessage, "MissingConfigurationException", e.getMessage()));
    		return null;
    	} catch (ConfigurationParseException e) {
    		log.error(StringUtils.format(errorMessage, "ConfigurationParseException", e.getMessage()));
    		return null;
    	} catch (ConfigurationMappingException e) {
    		log.error(StringUtils.format(errorMessage, "ConfigurationMappingException", e.getMessage()));
    		return null;
    	} catch (IOException e) {
    		log.error(StringUtils.format(errorMessage, "IOException", e.getMessage()));
    		return null;
    	} catch (WebDriverException e) {
    		log.error(StringUtils.format(errorMessage, "WebDriverException", e.getMessage()));
    		return null;
    	} catch (FileNotFoundException e) {
    		log.error(StringUtils.format(errorMessage, "FileNotFoundException", e.getMessage()));
    		return null;
		}
    	return driver;
    }
    /**
     * Returns the WebDriver instance
     * 
     * @return WebDriver
     * @throws WebDriverException if an error occurs when instantiating the WebDriver instance
     * @throws MissingConfigurationException if the requested configuration property has not been set
     * @throws ConfigurationParseException if error thrown while reading configuration file into sentinel
     * @throws ConfigurationMappingException if error thrown while mapping configuration file to sentinel
     * @throws IOException if other error occurs when mapping yml file into sentinel
     * @throws FileNotFoundException if the sentinel configuration file does not exist.
     */
    public static WebDriver getWebDriver() throws MissingConfigurationException, ConfigurationParseException, ConfigurationMappingException, IOException, WebDriverException, FileNotFoundException{
        if (instance == null) {
        	instantiateWebDriver();
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