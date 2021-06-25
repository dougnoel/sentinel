package com.dougnoel.sentinel.elements;

import java.util.Map;

/**
 * Textbox implementation of a PageElement.
 */
public class Textbox extends Element {

	/**
	 * Implementation of a PageElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param elementName String the name of the element
	 * @param selectors Map&lt;String,String&gt; the list of selectors to use to find the element
	 */
	public Textbox(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
	}
	
	/**
	 * Type text into a Textbox PageElement. Created as an alias for sendKeys.
	 * <p>
	 * <b>Alias For:</b> sendKeys(text)
	 * @param text String (text to type)
	 * @return PageElement (for chaining)
	 */
	public Element type(String text) {
        return this.sendKeys(text);
	}

	/**
	 * Gets the value entered into the textbox and returns it as a String.
	 * @return String The value entered into the textbox.
	 */
	@Override
	public String getText() {
		return element().getAttribute("value");
	}
}
