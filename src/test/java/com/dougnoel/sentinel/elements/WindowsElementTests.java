package com.dougnoel.sentinel.elements;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.NoSuchElementException;

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
	
	@Test(expected = NoSuchElementException.class)
	public void AccessibilityIDSelector() {
		getElement("generic_access").click();
	}
	
	@Test(expected = NoSuchElementException.class)
	public void IDSelector() {
		getElement("generic_id").click();
	}

	@Test(expected = NoSuchElementException.class)
	public void XPathSelector() {
		getElement("generic_xpath").click();
	}

	@Test(expected = InvalidSelectorException.class)
	public void BadSelector() {
		getElement("bad_selector");
	}
	
}