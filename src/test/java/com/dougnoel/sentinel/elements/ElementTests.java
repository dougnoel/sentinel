package com.dougnoel.sentinel.elements;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertFalse;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.steps.BaseSteps;
import com.dougnoel.sentinel.steps.TableVerificationSteps;
import com.dougnoel.sentinel.steps.TextVerificationSteps;
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

	
	@Test(expected = ElementNotInteractableException.class)
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
	
	@Test
	public void isHiddenOnHiddenElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertTrue("Expecting element to be hidden.", getElement("Hidden Field").isHidden());
	}
	
	@Test
	public void isHiddenOnNotDisplayedElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertTrue("Expecting element to be hidden.", getElement("Not Displayed Field").isHidden());
	}
	
	@Test
	public void isHiddenOnVisibleElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertFalse("Expecting element to be visible.", getElement("Last Name Field").isHidden());
	}
	
	@Test
	public void isDisplayedOnVisibleElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertTrue("Expecting element to be visible.", getElement("Last Name Field").isDisplayed());
	}
	
	@Test
	public void isDisplayedOnHiddenElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertFalse("Expecting element to be hidden.", getElement("Hidden Field").isDisplayed());
	}
	
	@Test
	public void isDisplayedOnNotDisplayedElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertFalse("Expecting element to be hidden.", getElement("Not Displayed Field").isDisplayed());
	}
	
	@Test
	public void isSelectedOnUnCheckedElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertFalse("Expecting element to not be selected.", getElement("Car Checkbox").isSelected());
	}

	@Test
	public void isSelectedOnCheckedElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertTrue("Expecting element to be selected.", getElement("Boat Checkbox").isSelected());
	}
	
	@Test
	public void isNotSelectedOnUnCheckedElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertTrue("Expecting element to not be selected.", getElement("Car Checkbox").isNotSelected());
	}

	@Test
	public void isNotSelectedOnCheckedElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertFalse("Expecting element to be selected.", getElement("Boat Checkbox").isNotSelected());
	}
	
	@Test
	public void doesNotExistOnVisibleElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertFalse("Expecting element to exist.", getElement("Last Name Field").doesNotExist());
	}
	
	@Test
	public void doesNotExistOnBadElement() {
		BaseSteps.navigateToPage("TextboxPage");
		assertTrue("Expecting element to not exist.", getElement("Bad Element").doesNotExist());
	}
	
	@Test(expected = ElementNotInteractableException.class)
	public void sendingTextToDisabledTextbox() {
		BaseSteps.navigateToPage("TextboxPage");
		getElement("First Name Field").sendKeys("stuff");
	}
	
	@Test
	public void sendingTextToHiddenTextbox() {
		BaseSteps.navigateToPage("TextboxPage");
		getElement("Hidden Field").sendKeys("stuff");
		assertTrue("Expecting hidden element to receive text.", getElement("Hidden Field").getText().contains("stuff"));
	}
	
	@Test(expected = NoSuchElementException.class)
	public void ElementDoesNotExist() {
		BaseSteps.navigateToPage("TextboxPage");
		getElement("Blah");
	}

	@Test(expected = InvalidSelectorException.class)
	public void InvalidSelectorTest() {
		BaseSteps.navigateToPage("TextboxPage");
		Map<String, String> selectors = new HashMap<>();
		selectors.put("value", "1");
		new Element("Checkbox", "Checkbox", selectors).element();
	}
	
	@Test
	public void sendSpecialKey() {
		BaseSteps.navigateToPage("KeyPressesPage");
		var input = (Textbox)getElement("key press input");
		input.sendKeys("A");
		input.sendSpecialKey(Keys.BACK_SPACE);
		TextVerificationSteps.verifyElementTextContains("result text", "", "contains", "BACK_SPACE");
		assertTrue("Expecting key press input to be empty.", input.getText().isEmpty());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void TableColumnDoesNotExist() {
		BaseSteps.navigateToPage("TablePage");
		TableVerificationSteps.verifyCellInSpecifiedRow("1", "Not a real column", "example table", "contains", "Bob");
	}
	
}
