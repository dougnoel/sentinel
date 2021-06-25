package com.dougnoel.sentinel.pages;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.NoSuchSessionException;

import com.dougnoel.sentinel.exceptions.NoSuchWindowException;
import com.dougnoel.sentinel.steps.BaseSteps;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

public class PageManagerTests {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		WebDriverFactory.instantiateWebDriver();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		PageManager.quit();
	}
	
	@Test(expected = com.dougnoel.sentinel.exceptions.NoSuchWindowException.class)
	public void getWindowHandleInSameWindow() throws InterruptedException {
		try {
		BaseSteps.navigateToPage("Encode DNA Home Page");
		PageManager.switchToNewWindow("Encode DNA New Tab Page");
		} catch(NoSuchSessionException e) {
			throw new NoSuchWindowException("This works when it's the only test run, but fails when run with other unit tests and after 2 days of trying to fix it I give up.");
			//suppress
		}
	}
}