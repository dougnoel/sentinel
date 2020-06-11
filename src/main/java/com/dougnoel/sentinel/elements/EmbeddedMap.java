package com.dougnoel.sentinel.elements;

import java.util.Map;

/**
 * Google Maps implementation of a Div WebElement.
 */
public class EmbeddedMap extends Div {

	/**
	 * Implementation of a PageElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param elementName String the name of the element
	 * @param selectors Map&lt;String,String&gt; the list of selectors to use to find the element
	 */
	public EmbeddedMap(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
	}
}
