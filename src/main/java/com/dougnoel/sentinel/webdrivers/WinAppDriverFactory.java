package com.dougnoel.sentinel.webdrivers;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebElement;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.MalformedURLException;

import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;

/**
 * @author dougnoel
 *
 *         Creates WinAppDriver.
 */
public class WinAppDriverFactory {
	private static final Logger log = LogManager.getLogger(WinAppDriverFactory.class);
	private static Process winAppDriverProcess = null;
	private static final String DRIVER_URL = "http://127.0.0.1:4723/wd/hub";
	private static final String COMMAND = "C:/Program Files (x86)/Windows Application Driver/WinAppDriver.exe";
	private static Integer numberOfDriversRunning = 0;
	private static final String STDOUT = "logs/WinAppDriver.log";
	private static final String STDERR = "logs/WinAppDriverError.log";
			
	/**
	 * Exists to defeat instantiation.
	 */
	private WinAppDriverFactory() {}

	private static URL getDriverUrl(){
		try {
			return new URL(DRIVER_URL);
		} catch (java.net.MalformedURLException e) {
			throw new MalformedURLException(e);
		}
	}

	/**
	 * Returns a newly created WindowsDriver as a WebDriver, based on the currently
	 * active page object and environment. If the WinAppDriver.exe program is not started,
	 * it also starts that.
	 * <p>
	 * Note: This method cannot tell if WinAppDriver.exe has already been started externally
	 * and will create a port conflict if it is already running.
	 * 
	 * @return WebDriver returns a WindowsDriver&lt;WebElement&gt;
	 */
	protected static WebDriver createWinAppDriver() {
		startWinAppDriverExe();
		URL url = getDriverUrl();

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("app", Configuration.executable());
		capabilities.setCapability("platformName", "Windows");
		capabilities.setCapability("deviceName", "WindowsPC");
		
		WindowsDriver<RemoteWebElement> driver = null;
		try {
			driver = new WindowsDriver<RemoteWebElement>(url, capabilities);
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

	/**
	 * Quits the WindowsDriver process passed to it. If this is the last remaining
	 * WinAppDriver running, the WinAppDriver.exe executable is also stopped.
	 * 
	 * @param driver WindowsDriver&lt;WebElement&gt; the WindowsDriver to quit
	 */
	protected static void quit(WindowsDriver<WindowsElement> driver) {
		driver.closeApp();
		numberOfDriversRunning -= 1;
		if (numberOfDriversRunning <= 0) {
			numberOfDriversRunning = 0;
			stopWinAppDriverExe();
		}
	}
	
	/**
	 * Starts the WinAppDriver executable program if it is not running
	 * so that Windows programs can be driven using it. Sets the
	 * winAppDrvierProcess member variable to be the running process.
	 * 
	 * TODO: Start this so that it can be left over after the test (parent process) ends.
	 */
	private static void startWinAppDriverExe() {
		if (winAppDriverProcess == null) {
			URL driverUrl = getDriverUrl();
			ProcessBuilder builder = new ProcessBuilder(COMMAND, 
														driverUrl.getHost(), 
														driverUrl.getPort() + driverUrl.getFile())
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
	
	/**
	 * Stops WinAppDriver running if it is running and sets the
	 * WinAppDriverProcess member variable to null so that we know
	 * we need to start it up again if necessary.
	 */
	private static void stopWinAppDriverExe() {
		if (winAppDriverProcess != null) {
			winAppDriverProcess.destroy();
			winAppDriverProcess = null;
		}
	}
}
