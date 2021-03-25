package com.dougnoel.sentinel.webdrivers;

import java.net.URL;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.MalformedURLException;

public class SauceLabsDriverFactory {

	private SauceLabsDriverFactory() {
		//exists to defeat instantiation
	}
	
    /**
     * Creates a single threaded Saucelabs WebDriver.
     * @return WebDriver a Saucelabs WebDriver
     */
    protected static WebDriver createSaucelabsDriver() {
        URL sauceLabsUrl;
		try {
			sauceLabsUrl = new URL("https://ondemand.saucelabs.com:443/wd/hub");
		} catch (java.net.MalformedURLException e) {
			throw new MalformedURLException(e);
		}
        
		String browser = Configuration.browser();
		String operatingSystem = Configuration.operatingSystem();
		
        MutableCapabilities options = new MutableCapabilities();
        options.setCapability("platform", operatingSystem);
        options.setCapability("browserName", browser);
        String browserVersion = Configuration.toString("browserVersion");
        if (browserVersion != null) {
        	options.setCapability("version", browserVersion);
        } else {
        	options.setCapability("version", "latest");
        }
        
        options.setCapability("username", Configuration.toString("saucelabsUserName"));
        options.setCapability("accesskey", Configuration.toString("saucelabsAccessKey"));
        
        setSaucelabsTestNameProperty(options);
        
        setOptionalSaucelabsProperty("parent-tunnel", options);
        setOptionalSaucelabsProperty("tunnelIdentifier", options);
        setOptionalSaucelabsProperty("tags", options);
        setOptionalSaucelabsProperty("build", options);
        
        return new RemoteWebDriver(sauceLabsUrl, options);
    }
    
    /**
     * Looks for a passed Saucelabs property name and sets it if it exists.
     * @param saucelabsPropertyName String the property to be set if it exists
     * @param options MutableCapabilities the MutableCapabilities object in which to set the properties
     */
    private static void setOptionalSaucelabsProperty(String saucelabsPropertyName, MutableCapabilities options) {
        String saucelabsProperty = Configuration.toString(saucelabsPropertyName);
        
        if (StringUtils.isNotEmpty(saucelabsProperty)) {
        	options.setCapability(saucelabsPropertyName, saucelabsProperty);
        }
    }
    
    /**
     * Pulls config values and sets a string to pass to Saucelabs for the job.
     * @param options MutableCapabilities object to modify
     */
    private static void setSaucelabsTestNameProperty(MutableCapabilities options)
    {
        String testName = "";
        String jobName = Configuration.toString("name");
        if (jobName != null) {
        	testName += "Name: " + jobName + " ";
        }
        String tags = Configuration.toString("tags");
        if (tags != null) {
        	testName += "Tags: " + tags + " ";
        }
        String userName = System.getProperty("user.name");
        if (userName != null) {
        	testName += "User: " + userName + " ";
        }
        String build = Configuration.toString("build");
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
    }
}
