package sentinel.elements;

import sentinel.utils.SelectorType;

/**
 * Button implementation of a PageElement.
 */
public class Button extends PageElement {

	/**
	 * Implementation of a PageElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param selectorType SelectorType (CSS, ID, NAME, TEXT, XPATH)
	 * @param selectorValue String the value used for selection
	 */
	public Button(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
	}

}
