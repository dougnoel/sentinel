package com.dougnoel.sentinel.elements.dropdowns;

import com.dougnoel.sentinel.enums.SelectorType;

/**
 * Dropdown implementation of a WebElement.
 */
public class Dropdown extends PageSelectElement {

	/**
	 * More common name for a select element.
	 * 
	 * @param selectorType SelectorType
	 * @param selectorValue String
	 */
	public Dropdown(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
	}
	
}
