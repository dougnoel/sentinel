package com.dougnoel.sentinel.elements.dropdowns;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

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
	 * Implementation of a JSDropdownElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param elementName String the name of the element
	 * @param selectors Map&lt;String,String&gt; the list of selectors to use to find the element
	 */
	public MaterialUISelect(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
	}

	/**
	 * Returns a WebElement for an option with the given text.
	 * @param selectionText String the text to be selected
	 * @return WebElement the element representing the option
	 */
	@Override
    protected WebElement getOption(String selectionText) {
    	String xPath = "//div[contains(@class, 'MuiPopover-root')]/div[3]/ul/li[contains(text(),'" + selectionText + "')]";
    	log.trace("Looking for the value in the dropdown at position {} using {}", selectionText, xPath);
    	this.click();
    	return this.element(By.xpath(xPath));
    }
    
    /**
     * Returns a WebElement for an option with the given index.
     * @param index int the index of the option, starting with 1
	 * @return WebElement the element representing the option
     */
	@Override
    protected WebElement getOption(int index) {
    	String xPath = "//div[contains(@class, 'MuiPopover-root')]/div[3]/ul/li[" + Integer.toString(index) + "]";
    	log.trace("Looking for the value in the dropdown at position {} using {}", index, xPath);
    	this.click();
    	return this.element(By.xpath(xPath));
    }

}
