package com.dougnoel.sentinel.pages;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.webdrivers.Driver;

public class PageDataTests {

	private final static String ELEMENT_NAME = "male_radio_button";

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Driver.quit();
	}
	
	@Test
	public void validateElementExistsInYaml() {
		PageManager.setPage("CorrectPageObject");
		Assert.assertNotNull("Expected page element to contain data.", Configuration.getElement(ELEMENT_NAME, "CorrectPageObject"));
	}
	
	@Test(expected = com.dougnoel.sentinel.exceptions.ConfigurationParseException.class)
	public void validateElementDoesNotExistsInYaml() {
		PageManager.setPage("IncorrectLocatorOffsetPageObject");
		getElement(ELEMENT_NAME);
	}
	
	@Test(expected = com.dougnoel.sentinel.exceptions.ConfigurationParseException.class)
	public void validateElementIsEmptyInYaml() throws IOException {
		PageManager.setPage("IncorrectElementOffsetPageObject");
		getElement(ELEMENT_NAME);
	}
	
	@Test(expected = com.dougnoel.sentinel.exceptions.ConfigurationParseException.class)
	public void validateSectionMissingInYaml() throws IOException {
		PageManager.setPage("MissingElementsSectionPageObject");
		getElement(ELEMENT_NAME);
	}

	@Test
	public void validateIncludeIsBlank() {
		PageManager.setPage("PageWithBlankInclude");
		Assert.assertNotNull("Expected text to be male when the include list is empty.", Configuration.getElement(ELEMENT_NAME, "PageWithBlankInclude"));
	}
	
}
