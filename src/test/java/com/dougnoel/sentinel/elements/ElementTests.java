package com.dougnoel.sentinel.elements;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dougnoel.sentinel.exceptions.ElementNotVisibleException;
import com.dougnoel.sentinel.steps.BaseSteps;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

public class ElementTests {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.setProperty("env", "dev");
		WebDriverFactory.instantiateWebDriver();
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		WebDriverFactory.quit();
	}

	
	@Test(expected = ElementNotVisibleException.class)
	public void clickOnDisabledTextbox() {
		BaseSteps.navigateToPage("TextboxPage");
		getElement("First Name Field").click();
	}
	
}
