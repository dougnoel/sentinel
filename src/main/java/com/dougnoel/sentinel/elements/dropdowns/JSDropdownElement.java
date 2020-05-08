package com.dougnoel.sentinel.elements.dropdowns;

import org.openqa.selenium.WebElement;

import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.exceptions.ElementNotFoundException;

/**
 * A number of angular and react libraries make their own dropdowns that are do not use
 * select elements. This class creates a base for implementing various javascript
 * dropdowns so that they can be treated as normal selenium dropdowns.
 * <br>
 * NOTE: This extends Dropdown and not PageSelectElement because there are plans
 * to have the dropdown type auto-detected in a later version.
 */
public abstract class JSDropdownElement extends Dropdown {

	/**
	 * Implementation of a WebElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param selectorType SelectorType
	 * @param selectorValue String
	 */
	public JSDropdownElement(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
	}

	/**
	 * Returns a WebElement for an option with the given text.
	 * @param selectionText String the text to be selected
	 * @return WebElement the element representing the option
	 * @throws ElementNotFoundException if the element cannot be found
	 */
    protected abstract WebElement getOption(String selectionText) throws ElementNotFoundException;
    
    /**
     * Returns a WebElement for an option with the given index.
     * @param index int the index of the option, starting with 1
	 * @return WebElement the element representing the option
	 * @throws ElementNotFoundException if the element cannot be found
     */
    protected abstract WebElement getOption(int index) throws ElementNotFoundException;
    
    /**
     * Selects an option from a drop down using the text value of the item to select.
     * @param selectionText the value to select
     * @return PageSelectElement for object chaining
     * @throws ElementNotFoundException if the element cannot be found
     */
    @Override
    public PageSelectElement select(String selectionText) throws ElementNotFoundException{
    	getOption(selectionText).click();
        return this;
    }

    /**
     * Selects an option from a drop down using the ordinal value of the item to select.
     * @param index the index to select
     * @return PageSelectElement for object chaining
     * @throws ElementNotFoundException if the element cannot be found
     */
    @Override
    public PageSelectElement select(int index) throws  ElementNotFoundException{
    	getOption(index).click();        
        return this;
    }

    /**
     * Gets the value of the item at the given index.
     * @param index the index to inspect, starting with 1
     * @return String the text value of the option at the given index
     * @throws ElementNotFoundException if the element cannot be found
     */
    @Override
    public String getText(int index) throws ElementNotFoundException{
    	return getOption(index).getText();
    }
    
    /**
     * Gets the text of the first item currently selected.
     * @return String the text value of the selected option
     * @throws ElementNotFoundException if the element cannot be found
     */
    @Override
    public String getSelectedText() throws ElementNotFoundException{
    	return this.element().getText();
    }
}
