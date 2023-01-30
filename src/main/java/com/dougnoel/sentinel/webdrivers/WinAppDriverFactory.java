package com.dougnoel.sentinel.webdrivers;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;
import java.util.Optional;

import com.dougnoel.sentinel.system.FileManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.system.JavaURL;

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
	private static final String DRIVER_URL = Configuration.toString("winAppDriverUrl", "http://127.0.0.1:4723/wd/hub");
	private static final String WINAPPDRIVER_PATH = Configuration.toString("winAppDriverPath", "C:/Program Files (x86)/Windows Application Driver/WinAppDriver.exe");
	private static Integer numberOfDriversRunning = 0;
	private static final String STDOUT = "logs/WinAppDriver.log";
	private static final String STDERR = "logs/WinAppDriverError.log";
			
	/**
	 * Exists to defeat instantiation.
	 */
	private WinAppDriverFactory() {}

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
		String executable = FileManager.winSpecialFolderConverter(Configuration.executable());

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("app", executable);
		capabilities.setCapability("platformName", "Windows");
		capabilities.setCapability("deviceName", "WindowsPC");
		
		WindowsDriver<WindowsElement> driver = null;
		try {
			driver = new WindowsDriver<>(JavaURL.create(DRIVER_URL), capabilities);
		}
		catch (Exception e) {
			stopWinAppDriverExe();
			log.error("{} Driver creation failed for: {}\n{}", e.getCause(), executable, e.getMessage());
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
	 * Future optimizations of this method are tracked in issues #284 and #285.
	 */
	private static void startWinAppDriverExe() {
		if (winAppDriverProcess == null) {
			URL driverUrl = JavaURL.create(DRIVER_URL);
			// Fully-qualified path to the executable is used here to resolve code smell security flag.
			ProcessBuilder builder = new ProcessBuilder(WINAPPDRIVER_PATH, 
														driverUrl.getHost(), 
														driverUrl.getPort() + driverUrl.getFile())
					.redirectInput(Redirect.INHERIT)
					.redirectOutput(new File(STDOUT))
					.redirectError(new File(STDERR));
			try {
				winAppDriverProcess = builder.start();	
				waitForDriverReady(winAppDriverProcess.pid());
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
			catch (IOException e) {
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


	private static boolean isReady(long pid){
		Optional<ProcessHandle> processHandle = ProcessHandle.of(pid);
		return processHandle.isPresent() && processHandle.get().isAlive();
	}

	private static void waitForDriverReady(long pid){
		long searchTime = Time.out().getSeconds() * 1000;
		long startTime = System.currentTimeMillis(); //fetch starting time
		
		while((System.currentTimeMillis() - startTime) < searchTime) {
			if(isReady(pid))
				return;
		}
		throw new com.dougnoel.sentinel.exceptions.IOException("Driver failed to create in allotted time.");
	}
}