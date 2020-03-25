package com.dougnoel.sentinel.elements;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.exceptions.ElementNotFoundException;
import com.dougnoel.sentinel.exceptions.NoSuchSelectorException;
import com.dougnoel.sentinel.strings.StringUtils;

/**
 * Implementation of an NGPrime Dropdown.
 * @see <a href="https://www.primefaces.org/primeng/showcase/#/dropdown">PrimeNG Dropdown</a>
 * <p>
 * <b>Page Object Examples:</b>
 * <ul>
 * <li>public PrimeNGDropdown city_dropdown() { return new PrimeNGDropdown(XPATH, "//p-dropdown[1]"); }</li>
 * <li>public PrimeNGDropdown state_dropdown() { return new PrimeNGDropdown(ID, "state"); }</li>
 * <li>public PrimeNGDropdown airport_dropdown() { return new PrimeNGDropdown(NAME, "airport"); }</li>
 * </ul>
 */
public class PrimeNGDropdown extends Dropdown {
	private static final Logger log = LogManager.getLogger(PrimeNGDropdown.class.getName());

	/**
	 * Implementation of a WebElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param selectorType SelectorType
	 * @param selectorValue String
	 */
	public PrimeNGDropdown(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
	}

    /**
     * Selects an option from a drop down using the text value of the item to select.
     * @param selectText the value to select
     * @return PageSelectElement for object chaining
     * @throws ElementNotFoundException if the element cannot be found
     */
    public PageSelectElement select(String selectText) throws ElementNotFoundException{
    	String xTagName = this.element().getTagName();
    	String xPath = "//li[@aria-label=\"" + selectText + "\"]";
    	log.trace("Trying to click option {} from downdown using the xpath {}{}", selectText, xTagName, xPath);
    	this.click();
    	this.element().findElement(By.xpath(xPath)).click();

        return this;
    }

    /**
     * Selects an option from a drop down using the ordinal value of the item to select.
     * @param index the index to select
     * @return PageSelectElement for object chaining
     * @throws ElementNotFoundException if the element cannot be found
     */
    public PageSelectElement select(int index) throws  ElementNotFoundException{
    	String xTagName = this.element().getTagName();
    	String xPath = "//p-dropdownitem[" + Integer.toString(index) + "]/li";
    	log.trace("Trying to click option {} from downdown using the xpath {}{}", index, xTagName, xPath);
    	this.click();
    	this.element().findElement(By.xpath(xPath)).click();
        
        return this;
    }

    /**
     * Selects an option from a drop down. Allows for selection by value in addition to 
     * selection by text or index.
     * @param selectorType com.dougnoel.sentinel.enums.SelectorType INDEX, VALUE, TEXT
     * @param selectText the value of the selector
     * @return PageSelectElement for object chaining
     * @throws ElementNotFoundException if the element cannot be found
     */
    public PageSelectElement select(SelectorType selectorType, String selectText) throws ElementNotFoundException {
        switch (selectorType) {
        case INDEX:
            return select(Integer.parseInt(selectText));
        case VALUE:
        	return select(selectText);
        case TEXT:
            return select(selectText);
        default:
            // This is here in case a new type is added to SelectorType and has not been
            // implemented yet here.
            String errorMessage = StringUtils.format(
                    "Unhandled selector type \"{}\" passed to Page Select Element class. Could not resolve the reference. Refer to the Javadoc for valid options.",
                    selectorType);
            throw new NoSuchSelectorException(errorMessage);
        }
    }

    /**
     * Gets the value of the item at the given index.
     * @param index the index to inspect, starting with 1
     * @return String the text value of the option at the given index
     * @throws ElementNotFoundException if the element cannot be found
     */
    public String getText(int index) throws ElementNotFoundException{
    	String xTagName = this.element().getTagName();
    	String xPath = "//p-dropdownitem[" + Integer.toString(index) + "]/li";
    	log.trace("Looking for the value in the dropdown at position {} using {}{}", index, xTagName, xPath);
    	this.element().click();
    	return this.element().findElement(By.xpath(xPath)).getAttribute("aria-label");
    }
    
    /**
     * Gets the text of the first item currently selected.
     * @return String the text value of the selected option
     * @throws ElementNotFoundException if the element cannot be found
     */
    public String getSelectedText() throws ElementNotFoundException{
    	return this.element().findElement(By.xpath("//label")).getText();
    }
}
