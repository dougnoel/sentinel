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

	/**
	 * Waits until the text contains a certain value, and returns if it was found
	 *
	 * @return Boolean If the text value was found in the element.
	 */
	@Override
	public Boolean waitForText(String text, boolean present) {
		ExpectedCondition<Boolean> condition = ExpectedConditions.textToBePresentInElementValue(element(), text);
		if (!present)
			condition = ExpectedConditions.not(ExpectedConditions.textToBePresentInElementValue(element(), text));

		long searchTime = Time.out().getSeconds() * 1000;
		long startTime = System.currentTimeMillis(); // fetch starting time

		while ((System.currentTimeMillis() - startTime) < searchTime) {
			try {
				return new WebDriverWait(Driver.getWebDriver(), Time.interval().toMillis(), Time.loopInterval().toMillis())
						.ignoring(StaleElementReferenceException.class)
						.ignoring(TimeoutException.class)
						.until(condition);
			} catch (TimeoutException e) {
				// suppressing this due to falsely thrown timeout exception
			}
		}
		return false;
	}
}