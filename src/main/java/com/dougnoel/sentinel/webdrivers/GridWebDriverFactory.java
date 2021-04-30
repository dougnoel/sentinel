package com.dougnoel.sentinel.webdrivers;

import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.exceptions.MalformedURLException;

public class GridWebDriverFactory {
	
	protected static WebDriver createGridDriver(String browser, String gridUrl) {
    	DesiredCapabilities capability = new DesiredCapabilities();
    	capability.setCapability("browserName", browser);
        String browserVersion = ConfigurationManager.getOptionalProperty("browserVersion");
    	if (browserVersion != null) 
    	    capability.setCapability("browserVersion", browserVersion);
    	
    	URL url;
        try {
			url = new URL(gridUrl);
		} catch (java.net.MalformedURLException e) {
			throw new MalformedURLException(e);
		}
        
        return new RemoteWebDriver(url, capability);
    }

}
