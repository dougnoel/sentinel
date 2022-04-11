package com.dougnoel.sentinel.webdrivers;

import org.junit.Test;

import com.dougnoel.sentinel.pages.PageManager;

public class DriverTests {

	@Test
	public void MaximizeWindowTest() {
		PageManager.setPage("CorrectPageObject");
		Driver.getWebDriver();
		Driver.maximizeWindow();
		Driver.quitAllDrivers();
	}

}
