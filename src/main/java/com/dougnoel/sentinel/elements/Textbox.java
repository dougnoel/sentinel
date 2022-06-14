package com.dougnoel.sentinel.elements;

import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.webdrivers.Driver;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Map;

/**
 * Textbox implementation of a Element. Needed because the text in a text box
 * is stored in a different place than all other elements.
 */
public class Textbox extends Element {

	/**
	 * Default Textbox constructor. An override of the Element constructor.
	 * 
	 * @param elementName String the name of the element
	 * @param selectors Map&lt;String,String&gt; the list of selectors to use to find the element
	 */
	public Textbox(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
	}

	/**
	 * Gets the value entered into the textbox and returns it as a String.
	 * 
	 * @return String the value entered into the textbox.
	 */
	@Override
	public String getText() {
		return element().getAttribute("value");
	}
}