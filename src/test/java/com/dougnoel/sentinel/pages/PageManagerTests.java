package com.dougnoel.sentinel.pages;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openqa.selenium.NotFoundException;

import com.dougnoel.sentinel.webdrivers.Driver;

public class PageManagerTests {

	@Test(expected = NotFoundException.class)
	public void PageNotSet() {
		PageManager.setPage(null);
		PageManager.getPage();
	}
	
	@Test
	public void OpenPageWithArguments() {
		PageManager.open("TextboxPage", "?stuff");
		assertEquals("https://dougnoel.github.io/sentinel/test/textbox.html?stuff", Driver.getWebDriver().getCurrentUrl());
		Driver.quitAllDrivers();
	} 

}
