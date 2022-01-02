package com.dougnoel.sentinel.elements;

import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import io.appium.java_client.MobileBy;

/**
 * WinAppDriver implementation of a Element.
 */
public class WindowsElement extends Element {

	/**
	 * Default Windows Element constructor. An override of the Element constructor.
	 * 
	 * @param elementName String the name of the element
	 * @param selectors Map&lt;String,String&gt; the list of selectors to use to find the element
	 */
	public WindowsElement(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
	}
	
	/**
	 * Returns the Selenium WebElement if it can be found on the current page.
	 * Provides late binding for elements so that the driver does not look for them
	 * until they are called, at which point the driver should be on the correct
	 * page.
	 * 
	 * @return org.openqa.selenium.WebElement the Selenium WebElement object type that can be acted upon
	 */
	@Override
	protected WebElement element() {
		WebElement element = null;
		long searchTime = Time.out().getSeconds() * 1000;
		long startTime = System.currentTimeMillis(); //fetch starting time
		while((System.currentTimeMillis() - startTime) < searchTime) {
			element = findElementInCurrentFrameForDuration(Time.loopInterval());
	    	if (element != null)
	    		return element;
        }
		var errorMessage = SentinelStringUtils.format("{} element named \"{}\" does not exist or is not visible using the following values: {}. Assure you are on the page you think you are on, and that the element identifier you are using is correct.",
				elementType, getName(), selectors);
		throw new NoSuchElementException(errorMessage);
	}

	/**
	 * Takes a Sentinel SelectorType and string value and returns a Selenium By locator
	 * specifically for Windows elements.
	 * 
	 * @param selectorType SelectorType the ENUM value indicating how we search for the element
	 * @param selectorValue String the value being pulled from the config file for the selector
	 * @return org.openqa.selenium.By returns a Selenium By selector for locating an element.
	 */
	@Override
	protected By createByLocator(SelectorType selectorType, String selectorValue) {
		try {
			switch (selectorType) {
			case ACCESSIBILITYID:
			case AUTOMATIONID:
				return MobileBy.AccessibilityId(selectorValue);
			case CLASS:
			case CLASSNAME:
				return By.className(selectorValue);
			case ID:
			case RUNTIMEID:
				return By.id(selectorValue);
			case NAME:
				return By.name(selectorValue);
			case TAGNAME:
			case LOCALIZEDCONTROLTYPE:
				return MobileBy.tagName(selectorValue);
			case XPATH:
				return By.xpath(selectorValue);
			default:
				var errorMessage = SentinelStringUtils.format("{} is not a valid selector type for WinAppDriver. Please fix the windows element {} in the {}.yml page object.", selectorType, getName(), PageManager.getPage().getName());
				
				log.error(errorMessage);
				throw new InvalidSelectorException(errorMessage);
			}
		} catch (IllegalArgumentException e) {
			var errorMessage = SentinelStringUtils.format("{}: {} is not a valid selector. Fix the element {} in the {}.yml page object.", selectorType, selectorValue, getName(), PageManager.getPage().getName());
			log.error(errorMessage);
			throw new InvalidSelectorException(errorMessage, e);
		}
	}
	
	/**
	 * Enter text into a Element. Typically used for text boxes.
	 * This method will throw an ElementDisabledException() if the text box is disabled.
	 * 
	 * @param text String the text to enter
	 * @return Element (for chaining)
	 */
	@Override
	public Element sendKeys(String text) {
		element().sendKeys(text);
		return this;
	}
	
	/**
	 * Returns the text of the page element as a String.
	 * 
	 * @return String the text value stored in the element	 */
	@Override
	public String getText() {
		return element().getText();
	}
	
	/**
	 * Click an Element.
	 * 
	 * @return Element (for chaining)
	 */
	@Override
	public Element click() {
		element().click();
		return this;
	} 
}
