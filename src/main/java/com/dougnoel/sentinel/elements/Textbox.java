package com.dougnoel.sentinel.elements;

import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.exceptions.ElementNotFoundException;

/**
 * Textbox implementation of a PageElement.
 */
public class Textbox extends PageElement {

	/**
	 * Implementation of a PageElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param selectorType SelectorType
	 * @param selectorValue String
	 */
	public Textbox(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
	}
	
	/**
	 * Type text into a Textbox PageElement. Created as an alias for sendKeys.
	 * <p>
	 * <b>Alias For:</b> sendKeys(text)
	 * @param text String (text to type)
	 * @return PageElement (for chaining)
	 * @throws ElementNotFoundException if the element is not found
	 */
	public PageElement type(String text) throws ElementNotFoundException {
        return this.sendKeys(text);
	}

	/**
	 * Gets the value entered into the textbox and returns it as a String.
	 * @return String The value entered into the textbox.
	 * @throws ElementNotFoundException if the eleemnt is not found
	 */
	public String getText() throws ElementNotFoundException {
		return element().getAttribute("value");
	}
}
