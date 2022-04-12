package com.dougnoel.sentinel.pages;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.NoSuchElementException;
import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.webdrivers.Driver;
import com.dougnoel.sentinel.exceptions.FileException;

public class PageDataTests {

	private final static String ELEMENT_NAME = "male_radio_button";

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Driver.quitAllDrivers();
	}
	
	@Test
	public void validateElementExistsInYaml() {
		PageManager.setPage("CorrectPageObject");
		Assert.assertNotNull("Expected page element to contain data.", Configuration.getElement(ELEMENT_NAME, "CorrectPageObject"));
	}
	
	@Test(expected = NoSuchElementException.class)
	public void validateElementDoesNotExistInYaml() {
		PageManager.setPage("IncorrectLocatorOffsetPageObject");
		getElement(ELEMENT_NAME);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void validateElementIsEmptyInYaml() {
		PageManager.setPage("IncorrectElementOffsetPageObject");
		getElement(ELEMENT_NAME);
	}
	
	@Test(expected = NoSuchElementException.class)
	public void validateSectionMissingInYaml() {
		PageManager.setPage("MissingElementsSectionPageObject");
		getElement(ELEMENT_NAME);
	}

	@Test
	public void validateIncludeIsBlank() {
		PageManager.setPage("PageWithBlankInclude");
		Assert.assertNotNull("Expected text to be male when the include list is empty.", Configuration.getElement(ELEMENT_NAME, "PageWithBlankInclude"));
	}
	
	@Test
	public void validateTestdataExistsInYaml() {
		PageManager.setPage("PageWithTestdata");
		Assert.assertNotNull("Expected testdata to contain data.", Configuration.getTestdataValue("report", "id"));
	}
	
	@Test(expected = FileException.class)
	public void validateTestdataMissingInYaml() {
		PageManager.setPage("CorrectPageObject");
		Configuration.getTestdataValue("report", "id");
	}

	@Test(expected = FileException.class)
	public void cannotContainBothURLsAndEXEs() {
		PageManager.setPage("WebAndExe");
		getElement("generic");
	}
	
}
