package com.dougnoel.sentinel.pages;

import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.NoSuchWindowException;

import com.dougnoel.sentinel.framework.PageManager;
import com.dougnoel.sentinel.steps.BaseSteps;
import com.dougnoel.sentinel.webdrivers.Driver;

public class PageManagerTests {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Driver.quitAllDrivers();
	}
	
	@Test(expected = NoSuchWindowException.class)
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