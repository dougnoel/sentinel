package com.dougnoel.sentinel.webdrivers;
import com.dougnoel.sentinel.system.FileManager;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import com.dougnoel.sentinel.configurations.Configuration;
import io.appium.java_client.windows.WindowsDriver;
import static io.appium.java_client.service.local.flags.GeneralServerFlag.BASEPATH;

/**
 * @author dougnoel
 *
 *         Creates WinAppDriver.
 */
public class WindowsDriverFactory {
	private static final Logger log = LogManager.getLogger(WindowsDriverFactory.class);
	private static Integer numberOfDriversRunning = 0;
	private static final String STDOUT = "logs/WinAppDriver.log";
	private static final String STDERR = "logs/WinAppDriverError.log";

	/**
	 * Exists to defeat instantiation.
	 */
	private WindowsDriverFactory() {}

	/**
	 * Creates a new Appium service and starts a new instance of WinAppDriver.exe associated to that service.
	 * <p>
	 * Returns a newly created WindowsDriver as a WebDriver, based on the currently
	 * active page object and environment.
	 * <p>
	 * Note: This method cannot tell if WinAppDriver.exe has already been started externally
	 * and will create a port conflict if it is already running.
	 *
	 * @return WebDriver returns a WindowsDriver&lt;WebElement&gt;
	 */
	protected static WebDriver createWindowsDriver() {
		String executable = FileManager.winSpecialFolderConverter(Configuration.executable());

		AppiumServiceBuilder builder = new AppiumServiceBuilder();
		builder.withIPAddress("127.0.0.1").withArgument(BASEPATH , "/wd/hub").usingPort(4725);
		AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("app", executable);
		capabilities.setCapability("forceMjsonwp", true);
		capabilities.setCapability("ms:experimental-webdriver", true);
		capabilities.setCapability("platformName", "Windows");
		capabilities.setCapability("automationName", "Windows");
		capabilities.setCapability("deviceName", "Windows10Machine");

		WindowsDriver driver = null;
		try {
			driver = new WindowsDriver(service, capabilities);
		}
		catch (Exception e) {
			log.error("{} Driver creation failed for: {}\n{}", e.getCause(), executable, e.getMessage());
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
		driver.quit();
		numberOfDriversRunning -= 1;
		if (numberOfDriversRunning <= 0) {
			numberOfDriversRunning = 0;
		}
	}
}