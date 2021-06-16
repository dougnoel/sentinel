package com.dougnoel.sentinel.elements.dropdowns;

import java.util.Map;

import org.openqa.selenium.WebElement;

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
	/**
	 * Implementation of a Dropdown to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param elementName String the name of the element
	 * @param selectors Map&lt;String,String&gt; the list of selectors to use to find the element
	 */
	protected JSDropdownElement(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
	}

	/**
	 * Returns a WebElement for an option with the given text.
	 * @param selectionText String the text to be selected
	 * @return WebElement the element representing the option
	 */
    protected abstract WebElement getOption(String selectionText);
    
    /**
     * Returns a WebElement for an option with the given index.
     * @param index int the index of the option, starting with 1
	 * @return WebElement the element representing the option
     */
    protected abstract WebElement getOption(int index);
    
    /**
     * Selects an option from a drop down using the text value of the item to select.
     * @param selectionText the value to select
     * @return PageSelectElement for object chaining
     */
    @Override
    public SelectElement select(String selectionText) {
    	getOption(selectionText).click();
        return this;
    }

    /**
     * Selects an option from a drop down using the ordinal value of the item to select.
     * @param index the index to select
     * @return PageSelectElement for object chaining
     */
    @Override
    public SelectElement select(int index) {
    	getOption(index).click();        
        return this;
    }

    /**
     * Gets the value of the item at the given index.
     * @param index the index to inspect, starting with 1
     * @return String the text value of the option at the given index
     */
    @Override
    public String getText(int index) {
    	return getOption(index).getText();
    }
    
    /**
     * Gets the text of the first item currently selected.
     * @return String the text value of the selected option
     */
    @Override
    public String getSelectedText() {
    	return this.element().getText();
    }
}
