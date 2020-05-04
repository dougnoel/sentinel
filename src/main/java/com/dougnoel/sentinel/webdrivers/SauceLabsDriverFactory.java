package com.dougnoel.sentinel.webdrivers;

import java.net.URL;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.exceptions.ConfigurationNotFoundException;
import com.dougnoel.sentinel.exceptions.MalformedURLException;
import com.dougnoel.sentinel.strings.StringUtils;

public class SauceLabsDriverFactory {

    /**
     * Creates a single threaded Saucelabs WebDriver.
     * @return WebDriver a Saucelabs WebDriver
     * @throws ConfigurationNotFoundException if a requested configuration property has not been set
     */
    protected static WebDriver createSaucelabsDriver() throws ConfigurationNotFoundException {
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
}
