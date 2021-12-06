package com.dougnoel.sentinel.elements;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.exceptions.ElementNotVisibleException;
import com.dougnoel.sentinel.steps.BaseSteps;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

public class ElementTests {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Time.reset();
		Configuration.update("timeout", 1);
		WebDriverFactory.instantiateWebDriver();
		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Time.reset();
		Configuration.update("timeout", 10);
		WebDriverFactory.quit();
	}

	
	@Test(expected = ElementNotVisibleException.class)
	public void clickOnDisabledTextbox() {
		BaseSteps.navigateToPage("TextboxPage");
		getElement("First Name Field").click();
	}
	
	@Test
	public void isDisabledOnDisabledElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertTrue("Expecting element to be disabled.", getElement("First Name Field").isDisabled());
	}
	
	@Test
	public void isDisabledOnEnabledElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertFalse("Expecting element to be enabled.", getElement("Last Name Field").isDisabled());
	}	
	
	@Test
	public void isEnabledOnDisabledElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertFalse("Expecting element to be disabled.", getElement("First Name Field").isEnabled());
	}
	
	@Test
	public void isEnabledOnEnabledElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertTrue("Expecting element to be enabled.", getElement("Last Name Field").isEnabled());
	}
}
