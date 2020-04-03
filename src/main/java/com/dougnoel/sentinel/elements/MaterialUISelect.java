package com.dougnoel.sentinel.elements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.exceptions.ElementNotFoundException;

/**
 * Implementation of an Material-UI Select.
 * @see <a href="https://material-ui.com/components/selects/">Material-UI Select</a>
 * <p>
 * <b>Page Object Examples:</b>
 * <ul>
 * <li>public MaterialUISelect city_dropdown() { return new MaterialUISelect(XPATH, "//p-dropdown[1]"); }</li>
 * <li>public MaterialUISelect state_dropdown() { return new MaterialUISelect(ID, "state"); }</li>
 * <li>public MaterialUISelect airport_dropdown() { return new MaterialUISelect(NAME, "airport"); }</li>
 * </ul>
 */
public class MaterialUISelect extends JSDropdownElement {
	private static final Logger log = LogManager.getLogger(MaterialUISelect.class.getName());

	/**
	 * Implementation of a WebElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param selectorType SelectorType
	 * @param selectorValue String
	 */
	public MaterialUISelect(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
	}

	/**
	 * Returns a WebElement for an option with the given text.
	 * @param selectionText String the text to be selected
	 * @return WebElement the element representing the option
	 * @throws ElementNotFoundException if the element cannot be found
	 */
    protected WebElement getOption(String selectionText) throws ElementNotFoundException{
    	String xPath = "//div[contains(@class, 'MuiPopover-root')]/div[3]/ul/li[contains(text(),'" + selectionText + "')]";
    	log.trace("Looking for the value in the dropdown at position {} using {}", selectionText, xPath);
    	this.click();
    	return driver.findElement(By.xpath(xPath));
    }
    
    /**
     * Returns a WebElement for an option with the given index.
     * @param index int the index of the option, starting with 1
	 * @return WebElement the element representing the option
	 * @throws ElementNotFoundException if the element cannot be found
     */
    protected WebElement getOption(int index) throws ElementNotFoundException{
    	String xPath = "//div[contains(@class, 'MuiPopover-root')]/div[3]/ul/li[" + Integer.toString(index) + "]";
    	log.trace("Looking for the value in the dropdown at position {} using {}", index, xPath);
    	this.click();
    	return driver.findElement(By.xpath(xPath));
    }

}
