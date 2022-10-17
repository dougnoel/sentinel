package com.dougnoel.sentinel.webdrivers;

import static org.junit.Assert.assertSame;

import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;

import com.dougnoel.sentinel.pages.PageManager;
import org.openqa.selenium.WebDriver;

public class DriverTests {

	@Test
	public void MaximizeWindowTest() {
		PageManager.setPage("CorrectPageObject");
		var js = (JavascriptExecutor)Driver.getWebDriver();
		Driver.maximizeWindow();
		assertSame("Expecting window to be maximized.", "false", js.executeScript("return document.hidden").toString());
		Driver.quitAllDrivers();
	}

	@Test
	public void NewDriverTest() {
		WebDriver newDriver = Driver.getWebDriver();
		Driver.setWebDriver((newDriver));
	}
}
