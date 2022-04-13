package com.dougnoel.sentinel.dropdowns;

import org.openqa.selenium.InvalidSelectorException;
import static com.dougnoel.sentinel.elements.ElementFunctions.getElementAsDropdown;
import static org.junit.Assert.assertEquals;
import org.junit.AfterClass;
import org.junit.Test;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.steps.BaseSteps;
import com.dougnoel.sentinel.webdrivers.Driver;

public class SelectElementTests {

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Driver.quitAllDrivers();
	}
	
	@Test(expected = InvalidSelectorException.class)
	public void SelectElementBadSelectorTest() {
		BaseSteps.navigateToPage("DropdownPage");
		getElementAsDropdown("Dropdown").select(SelectorType.XPATH, "Option 1");
	}
	
	@Test
	public void SelectAsValueTest() {
		BaseSteps.navigateToPage("DropdownPage");
		getElementAsDropdown("Dropdown").select(SelectorType.VALUE, "1");
		assertEquals("Expecting select element to be selected.", "Option 1", getElementAsDropdown("Dropdown").getSelectedText());
	}

}