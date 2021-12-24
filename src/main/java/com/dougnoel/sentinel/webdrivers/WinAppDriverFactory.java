package com.dougnoel.sentinel.webdrivers;

import java.net.URL;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.MalformedURLException;

import io.appium.java_client.windows.WindowsDriver;

/**
 * @author dougnoel
 *
 * Creates WinAppDriver.
 */
public class WinAppDriverFactory {

	private WinAppDriverFactory() {
		//exists to defeat instantiation
	}
	
//    protected static WebDriver createWinAppDriver() {
    	public static WebDriver createWinAppDriver() {
    		URL url;
    		try {
				url = new URL("http://127.0.0.1:4723");
			} catch (java.net.MalformedURLException e) {
				throw new MalformedURLException(e);
			}

			 DesiredCapabilities capabilities = new DesiredCapabilities();
			 capabilities.setCapability("app", Configuration.executable());
			 capabilities.setCapability("platformName","Windows");
			 capabilities.setCapability("deviceName", "WindowsPC");			 
			
			 return new WindowsDriver<WebElement>(url, capabilities);
    }

}
