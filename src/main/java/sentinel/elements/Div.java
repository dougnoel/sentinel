package sentinel.elements;

import sentinel.utils.SelectorType;

/**
 * Div implementation of a WebElement.
 */
public class Div extends PageElement {
	
	/**
	 * Implementation of a WebElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param selectorType SelectorType
	 * @param selectorValue String
	 */
	public Div(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
	}
}