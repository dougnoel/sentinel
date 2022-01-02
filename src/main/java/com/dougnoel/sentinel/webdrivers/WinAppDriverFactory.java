package com.dougnoel.sentinel.webdrivers;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.MalformedURLException;

import io.appium.java_client.windows.WindowsDriver;

/**
 * @author dougnoel
 *
 *         Creates WinAppDriver.
 */
public class WinAppDriverFactory {
	private static final Logger log = LogManager.getLogger(WinAppDriverFactory.class);
	private static Process winAppDriverProcess = null;
	private static final String COMMAND = "C:/Program Files (x86)/Windows Application Driver/WinAppDriver.exe";
	private static Integer numberOfDriversRunning = 0;
	private static final String STDOUT = "logs/WinAppDriver.log";
	private static final String STDERR = "logs/WinAppDriverError.log";
			

	private WinAppDriverFactory() {
		// exists to defeat instantiation
	}

	protected static WebDriver createWinAppDriver() {
		startWinAppDriverExe();
		URL url;
		try {
			url = new URL("http://127.0.0.1:4723");
		} catch (java.net.MalformedURLException e) {
			throw new MalformedURLException(e);
		}

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("app", Configuration.executable());
		capabilities.setCapability("platformName", "Windows");
		capabilities.setCapability("deviceName", "WindowsPC");
		
		WindowsDriver<WebElement> driver = null;
		try {
			driver = new WindowsDriver<WebElement>(url, capabilities);
		}
		catch (Exception e) {
			stopWinAppDriverExe();
			log.error("Driver creation failed.\n{}",e);
			throw e;
		}
		
		log.info("Driver created: {}\nLog Location:       {}\nError Log Location: {}", driver, STDOUT, STDERR);
		numberOfDriversRunning += 1;
		return driver;
	}

	protected static void quit(WindowsDriver<WebElement> driver) {
		driver.closeApp();
		numberOfDriversRunning -= 1;
		if (numberOfDriversRunning <= 0) {
			numberOfDriversRunning = 0;
			stopWinAppDriverExe();
		}
	}
	
	private static void startWinAppDriverExe() {
		if (winAppDriverProcess == null) {
			ProcessBuilder builder = new ProcessBuilder(COMMAND)
					.redirectInput(Redirect.INHERIT)
					.redirectOutput(new File(STDOUT))
					.redirectError(new File(STDERR));
			try {
				winAppDriverProcess = builder.start();
			} catch (IOException e) {
				throw new com.dougnoel.sentinel.exceptions.IOException(e);
			}
		}
	}
	
	private static void stopWinAppDriverExe() {
		if (winAppDriverProcess != null) {
			winAppDriverProcess.destroy();
			winAppDriverProcess = null;
		}
	}
}
