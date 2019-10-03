package com.dougnoel.sentinel.elements;

import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.exceptions.NoSuchElementException;
import com.dougnoel.sentinel.exceptions.NoSuchSelectorException;

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
	 * @throws NoSuchElementException if the element cannot be found
	 * @throws NoSuchSelectorException if the selector type passed is invalid
	 */
	public PageElement type(String text) throws NoSuchSelectorException, NoSuchElementException {
        return this.sendKeys(text);
	}

	/**
	 * Gets the value entered into the textbox and returns it as a String.
	 * @return String The value entered into the textbox.
	 * @throws NoSuchElementException if the element cannot be found
	 * @throws NoSuchSelectorException if the selector type passed is invalid
	 */
	public String getText() throws NoSuchSelectorException, NoSuchElementException {
		return element().getAttribute("value");
	}
}
