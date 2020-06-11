package com.dougnoel.sentinel.elements;

import java.util.Map;

import com.dougnoel.sentinel.exceptions.ConfigurationNotFoundException;
import com.dougnoel.sentinel.exceptions.ElementNotFoundException;

/**
 * Check box implementation of a PageElement.
 */
public class Checkbox extends PageElement {

	/**
	 * Implementation of a PageElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param elementName String the name of the element
	 * @param selectors Map&lt;String,String&gt; the list of selectors to use to find the element
	 */
	public Checkbox(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
	}
	
	/**
	 * Check a Checkbox PageElement. Created as an alias for click.
	 * <p>
	 * <b>Alias For:</b> PageElement.click()
	 * @return PageElement (for chaining)
	 * @throws ConfigurationNotFoundException if the requested configuration property has not been set
	 * @throws ElementNotFoundException if the element is not found
	 */
	public PageElement check() {
		return this.click();
	}
		
	/**
	 * Un-check a Checkbox PageElement. Created as an alias for clear.
	 * <p>
	 * <b>Alias For:</b> PageElement.clear()
	 * @return PageElement (for chaining)
	 * @throws ElementNotFoundException if the eleemnt is not found
	 */
	public PageElement uncheck() {
		return this.clear();
	}

}
