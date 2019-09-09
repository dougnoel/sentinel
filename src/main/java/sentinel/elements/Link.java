package sentinel.elements;

import sentinel.utils.SelectorType;

/**
 * Hyperlink (Link) implementation of a WebElement.
 */
public class Link extends PageElement {

	/**
	 * Implementation of a WebElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param selectorType SelectorType
	 * @param selectorValue String
	 */
	public Link(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
	}

}
