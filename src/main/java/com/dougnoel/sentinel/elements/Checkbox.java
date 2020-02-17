package com.dougnoel.sentinel.elements;

import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.exceptions.ConfigurationNotFoundException;
import com.dougnoel.sentinel.exceptions.ElementNotFoundException;

/**
 * Check box implementation of a PageElement.
 */
public class Checkbox extends PageElement {

	/**
	 * 
	 * @param selectorType (CSS, ID, NAME, TEXT, XPATH)
	 * @param selectorValue the value of the selector we are using to access the checkbox
	 */
	public Checkbox(SelectorType selectorType, String selectorValue){
		super(selectorType, selectorValue);
	}
	
	/**
	 * Check a Checkbox PageElement. Created as an alias for click.
	 * <p>
	 * <b>Alias For:</b> PageElement.click()
	 * @return PageElement (for chaining)
	 * @throws ConfigurationNotFoundException if the requested configuration property has not been set
	 * @throws ElementNotFoundException if the element is not found
	 */
	public PageElement check() throws ConfigurationNotFoundException, ElementNotFoundException {
		return this.click();
	}
		
	/**
	 * Un-check a Checkbox PageElement. Created as an alias for clear.
	 * <p>
	 * <b>Alias For:</b> PageElement.clear()
	 * @return PageElement (for chaining)
	 * @throws ElementNotFoundException if the eleemnt is not found
	 */
	public PageElement uncheck() throws ElementNotFoundException{
		return this.clear();
	}

}
