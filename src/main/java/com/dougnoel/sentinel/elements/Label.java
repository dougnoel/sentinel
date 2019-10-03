package com.dougnoel.sentinel.elements;

import com.dougnoel.sentinel.enums.SelectorType;

/**
 * Div implementation of a WebElement.
 */
public class Label extends PageElement {
	
	/**
	 * Implementation of a WebElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param selectorType SelectorType
	 * @param selectorValue String
	 */
	public Label(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
	}
}