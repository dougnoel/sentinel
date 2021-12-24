package com.dougnoel.sentinel.elements;

import java.util.Map;

/**
 * WinAppDriver implementation of a Element.
 */
public class WindowsElement extends Element {

	/**
	 * Default Windows Element constructor. An override of the Element constructor.
	 * 
	 * @param elementName String the name of the element
	 * @param selectors Map&lt;String,String&gt; the list of selectors to use to find the element
	 */
	public WindowsElement(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
	}

}
