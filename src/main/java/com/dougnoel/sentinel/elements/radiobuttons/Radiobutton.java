package com.dougnoel.sentinel.elements.radiobuttons;

import com.dougnoel.sentinel.elements.PageElement;
import com.dougnoel.sentinel.enums.SelectorType;

/**
 * Radio button implementation of a PageSelectElement.
 */
public class Radiobutton extends PageElement {

	/**
	 * Implementation of a PageElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class.
	 * 
	 * @param selectorType SelectorType
	 * @param selectorValue String
	 */
	public Radiobutton(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
	}

}
