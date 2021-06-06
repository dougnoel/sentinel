package com.dougnoel.sentinel.webdrivers;

import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.MalformedURLException;

public class GridWebDriverFactory {
	
	private static final String BROWSERNAME = "browserName";
	private static final String BROWSERVERSION = "browserVersion";
	
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
    	capability.setCapability(BROWSERNAME, browser);
        var browserVersion = Configuration.toString(BROWSERVERSION);
        
    	if (browserVersion != null) 
    	    capability.setCapability(BROWSERVERSION, browserVersion);
    	else {
    		capability.setCapability(BROWSERNAME, "firefox");
    		capability.setCapability(BROWSERVERSION, getDefaultBrowserVersion(url));
    	}
    	
        
        return new RemoteWebDriver(url, capability);
    }
	
	private static String getDefaultBrowserVersion(URL url){
		var capability = new DesiredCapabilities();
    	capability.setCapability(BROWSERNAME, "firefox");
		var driver = new RemoteWebDriver(url, capability);
		var cap = driver.getCapabilities();
		driver.quit();
		
		return cap.getVersion();
	}
}
