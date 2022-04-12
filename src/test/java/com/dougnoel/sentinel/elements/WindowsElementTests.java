package com.dougnoel.sentinel.elements;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import java.util.HashMap;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.InvalidSelectorException;
import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.steps.BaseSteps;
import com.dougnoel.sentinel.webdrivers.Driver;


public class WindowsElementTests {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Time.reset();
		Configuration.update("timeout", 1);
		BaseSteps.navigateToPage("WindowsElements");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Time.reset();
		Configuration.update("timeout", 10);
		Driver.quitAllDrivers();
	}

	@Test(expected = InvalidSelectorException.class)
	public void InvalidSelectorTest() {
		Map<String, String> selectors = new HashMap<>();
		selectors.put("value", "1");
		new WindowsElement("Foo", selectors).element();
	}
	
	@Test
	public void AccessibilityIDSelector() {
		Assert.assertNotNull("Accessibility Id selector should work.", getElement("generic_access"));
	}
	
	@Test
	public void IDSelector() {
		Assert.assertNotNull("Id selector should work.", getElement("generic_id"));
	}

	@Test
	public void XPathSelector() {
		Assert.assertNotNull("XPath selector should work.", getElement("generic_xpath"));
	}

	@Test(expected = InvalidSelectorException.class)
	public void BadSelector() {
		getElement("bad_selector");
	}
	
}
