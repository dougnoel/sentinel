package com.dougnoel.sentinel.webdrivers;

import java.io.File;
import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;
import java.net.URL;
import java.util.Optional;

import com.dougnoel.sentinel.system.FileManager;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.system.JavaURL;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.windows.WindowsDriver;

import static io.appium.java_client.service.local.flags.GeneralServerFlag.BASEPATH;
//import io.appium.java_client.windows.WindowsElement;

/**
 * @author dougnoel
 *
 *         Creates WindowsDriver.
 *         Change to use Appium Driver instantiation
 */
public class WindowsDriverFactory {
	private static final Logger log = LogManager.getLogger(WindowsDriverFactory.class);
	private static Process winAppDriverProcess = null;
	//private static final String DRIVER_URL = Configuration.toString("winAppDriverUrl", "http://127.0.0.1:4723"); //http://127.0.0.1:4723/wd/hub
	//private static final String WINAPPDRIVER_PATH = Configuration.toString("winAppDriverPath", "C:/Program Files/Windows Application Driver/WinAppDriver.exe");
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

		DesiredCapabilities capabilities = new DesiredCapabilities();
		capabilities.setCapability("appium:app", executable);
		capabilities.setCapability("appium:platformName", "windows");
		capabilities.setCapability("appium:automationName", "windows");
		capabilities.setCapability("appium:createSessionTimeout", "20000");
		capabilities.setCapability("ms:waitForAppLaunch", "30000");
		capabilities.setCapability("deviceName", "WindowsPC");
		
		WindowsDriver driver = null;
		//try {
			driver = new WindowsDriver(/*JavaURL.create("http://127.0.0.1:4723"),*/ capabilities); //JavaURL.create(DRIVER_URL),
		/*}
		catch (Exception e) {
			//stopWinAppDriverExe();
			log.error("{} Driver creation failed for: {}\n{}", e.getCause(), executable, e.getMessage());
			throw e;
		}*/
		
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
		/*if (winAppDriverProcess == null) {
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
		}*/

		AppiumServiceBuilder builder = new AppiumServiceBuilder();
		//builder.withIPAddress("127.0.0.1").usingPort(4725);
		builder.withArgument (BASEPATH, "/wd/hub");
		//.withArgument (GeneralServerFlag.SESSION_OVERRIDE)
		//.withArgument (GeneralServerFlag.LOG_LEVEL, "debug");
		//AppiumDriverLocalService service = AppiumDriverLocalService.buildDefaultService();   //buildService(builder);
		//AppiumDriverLocalService service = AppiumDriverLocalService.buildService(builder);
		//service.withBasePath("/wd/hub/");
		//service.getBasePath();
		//service.getUrl();
		//service.start();

		/*CommandLine cmd = new CommandLine("C:\\Program Files (x86)\\Appium\\node.exe");
		cmd.addArgument("C:\\Program Files (x86)\\Appium\\node_modules\\appium\\bin\\Appium.js");
		cmd.addArgument("--address");
		cmd.addArgument("127.0.0.1");
		cmd.addArgument("--port");
		cmd.addArgument("4723");

		DefaultExecuteResultHandler handler = new DefaultExecuteResultHandler();
		DefaultExecutor executor = new DefaultExecutor();
		executor.setExitValue(1);
		try {
			executor.execute(cmd, handler);
			Thread.sleep(10000);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
*/

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
//
//
//
//	private static boolean isReady(long pid){
//		Optional<ProcessHandle> processHandle = ProcessHandle.of(pid);
//		return processHandle.isPresent() && processHandle.get().isAlive();
//	}
//
//	private static void waitForDriverReady(long pid){
//		long searchTime = Time.out().getSeconds() * 1000;
//		long startTime = System.currentTimeMillis(); //fetch starting time
//
//		while((System.currentTimeMillis() - startTime) < searchTime) {
//			if(isReady(pid))
//				return;
//		}
//		throw new com.dougnoel.sentinel.exceptions.IOException("Driver failed to create in allotted time.");
//
//	}
//	*/
}