package com.dougnoel.sentinel.pages;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.ConfigurationParseException;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

public class PageDataTests {

	private static WebDriver driver;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		driver = WebDriverFactory.instantiateWebDriver();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		driver.close();
	}
	
	@Test
	public void validateElementsExistsInYml() {
		PageManager.setPage("PageObjStructure1");
		Assert.assertFalse(Configuration.getElement("male_radio_button", PageManager.getPage().getName()).isEmpty());		
	}
	
	@Test(expected = com.dougnoel.sentinel.exceptions.ConfigurationParseException.class)
	public void validateElementsDoesntExistsInYml() {
		PageManager.setPage("PageObjStructure2");
		Configuration.getElement("male_radio_button", PageManager.getPage().getName());
	}
	
	@Test(expected = com.dougnoel.sentinel.exceptions.ConfigurationParseException.class)
	public void validateElementsEmptyInYml() throws IOException {
		PageManager.setPage("PageObjStructure3");
		Configuration.getElement("male_radio_button", PageManager.getPage().getName());
	}
}
