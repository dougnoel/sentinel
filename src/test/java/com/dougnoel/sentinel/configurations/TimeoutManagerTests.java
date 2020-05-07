package com.dougnoel.sentinel.configurations;

import static org.junit.Assert.*;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

public class TimeoutManagerTests {

	@Test
	public void useDefaultTimeout() {
		assertEquals("Default timeout.", 10L, TimeoutManager.getDefaultTimeout());
	}
	
	@Test
	public void useDefaultTimeunit() {
		assertEquals("Default timeunit", TimeUnit.SECONDS, TimeoutManager.getDefaultTimeUnit());
	}

/* These two tests will fail unless they are the only unit tests run. Thre's no elegant way to handle this with
 * JUnit 4, so I've left them commented out but still included just in case. You will have to comment out the first
 * two tests and uncomment these to run them, and then run only this file. These values can only be set once, the
 * first time the class is initialized.
 */
//	@Test
//	public void useCustomTimeout() {
//		System.setProperty("timeout", "20");
//		System.setProperty("timeunit", "MILLISECONDS");
//		assertEquals("Custom timeout", 20L, TimeoutManager.getDefaultTimeout());
//	}
//
//	@Test
//	public void useCustomTimeunit() {
//		System.setProperty("timeout", "20");
//		System.setProperty("timeunit", "MILLISECONDS");
//		assertEquals("Custom timeunit", TimeUnit.MILLISECONDS, TimeoutManager.getDefaultTimeUnit());
//	}
	
	@Test(expected = Test.None.class /* no exception expected */)
	public void setTimeout() throws Throwable {
		System.setProperty("env", "dev");
		WebDriver driver = WebDriverFactory.instantiateWebDriver();
		TimeoutManager.setDefaultTimeout(driver);
		driver.close();
	}

}
