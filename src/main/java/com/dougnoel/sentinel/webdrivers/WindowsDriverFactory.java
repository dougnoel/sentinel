package com.dougnoel.sentinel.webdrivers;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;
import java.util.Optional;

import com.dougnoel.sentinel.system.FileManager;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.ServerArgument;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.system.JavaURL;

import io.appium.java_client.windows.WindowsDriver;

import static io.appium.java_client.service.local.flags.GeneralServerFlag.BASEPATH;

/**
 * @author dougnoel
 *
 *         Creates WinAppDriver.
 */
public class WindowsDriverFactory {
	private static final Logger log = LogManager.getLogger(WindowsDriverFactory.class);
	private static Process winAppDriverProcess = null;
	//private static final String DRIVER_URL = Configuration.toString("winAppDriverUrl", "http://127.0.0.1:4723/wd/hub");
	//private static final String WINAPPDRIVER_PATH = Configuration.toString("winAppDriverPath", "C:/Program Files (x86)/Windows Application Driver/WinAppDriver.exe");
	private static Integer numberOfDriversRunning = 0;
	private static final String STDOUT = "logs/WinAppDriver.log";
	private static final String STDERR = "logs/WinAppDriverError.log";

	/**
	 * Exists to defeat instantiation.
	 */
	private WindowsDriverFactory() {}

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

		AppiumServiceBuilder builder = new AppiumServiceBuilder();
		builder.withIPAddress("127.0.0.1").usingPort(4725);
		//builder.withArgument(BASEPATH , "/wd/hub");

		AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);
		//service.withBasePath("");*/


		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("app", executable);
//		capabilities.setCapability("ms:experimental-webdriver", true);
		//capabilities.setCapability("app", "C:\\Windows\\System32\\calc.exe");
		//capabilities.setCapability("appium:app", "C:\\Windows\\System32\\calc.exe");
		capabilities.setCapability("platformName", "Windows");
		capabilities.setCapability("automationName", "Windows");
//		capabilities.setCapability("deviceName", "Windows10Machine");

		WindowsDriver driver = null;
		try {
			driver = new WindowsDriver(service, capabilities);
			//driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
		}
		catch (Exception e) {
			//stopWinAppDriverExe();
			log.error("{} Driver creation failed for: {}\n{}", e.getCause(), executable, e.getMessage());
			//throw e;
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
	protected static void quit(WindowsDriver driver) {
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