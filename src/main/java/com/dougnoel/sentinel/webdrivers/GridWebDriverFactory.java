package com.dougnoel.sentinel.webdrivers;

import java.net.URL;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.MalformedURLException;

public class GridWebDriverFactory {
	
	private GridWebDriverFactory() {
	}
	
	protected static WebDriver createGridDriver(String browser, String gridUrl) {
    	URL url;
    	
        try {
			url = new URL(gridUrl);
		} catch (java.net.MalformedURLException e) {
			throw new MalformedURLException(e);
		}

		var capability = new DesiredCapabilities();
    	capability.setCapability("browserName", browser);
        var browserVersion = Configuration.toString("browserVersion");
        
    	if (browserVersion != null) 
    	    capability.setCapability("browserVersion", browserVersion);
    	else {
    		capability.setCapability("browserName", "firefox");
    		capability.setCapability("browserVersion", getDefaultBrowserVersion(url));
    	}
    	
        
        return new RemoteWebDriver(url, capability);
    }
	
	private static String getDefaultBrowserVersion(URL url){
		var capability = new DesiredCapabilities();
    	capability.setCapability("browserName", "firefox");
		var driver = new RemoteWebDriver(url, capability);
		var cap = ((RemoteWebDriver)driver).getCapabilities();
		driver.quit();
		
		return cap.getVersion();
	}
	
}
