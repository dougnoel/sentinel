package com.dougnoel.sentinel.elements;

import com.dougnoel.sentinel.enums.SelectorType;

/**
 * Google Maps implementation of a Div WebElement.
 */
public class GoogleMap extends Div {

	/**
	 * Implementation of a Div WebElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * Specific Constructor for a Google Maps div containing a Google Maps reference.
	 * 
	 * @param selectorType SelectorType
	 * @param selectorValue String
	 */
	public GoogleMap(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
	}
}
