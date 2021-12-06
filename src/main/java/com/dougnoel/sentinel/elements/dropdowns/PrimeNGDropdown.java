package com.dougnoel.sentinel.elements.dropdowns;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.dougnoel.sentinel.configurations.Time;

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
public class PrimeNGDropdown extends JSDropdownElement {
	private static final Logger log = LogManager.getLogger(PrimeNGDropdown.class.getName());
	
	private static final double SELECTWAITTIME = 0.1; //Selenium is faster than NGPrime, and so we needed to allow it to catch up for drop down selection

	/**
	 * Implementation of a JSDropdownElement to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param elementName String the name of the element
	 * @param selectors Map&lt;String,String&gt; the list of selectors to use to find the element
	 */
	public PrimeNGDropdown(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
	}

	/**
	 * Returns a WebElement for an option with the given text.
	 * @param selectionText String the text to be selected
	 * @return WebElement the element representing the option
	 */
	@Override
    protected WebElement getOption(String selectionText) {
		Time.wait(SELECTWAITTIME);
		String xTagName = this.element().getTagName();
    	String xPath = "//li[@aria-label=\"" + selectionText + "\"]";
    	log.trace("Trying to click option {} from downdown using the xpath {}{}", selectionText, xTagName, xPath);
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
		Time.wait(SELECTWAITTIME);
    	String xTagName = this.element().getTagName();
    	String xPath = "//p-dropdownitem[" + Integer.toString(index) + "]/li";
    	log.trace("Trying to click option {} from downdown using the xpath {}{}", index, xTagName, xPath);
    	this.click();
    	return this.element(By.xpath(xPath));
    }
    

    /**
     * Gets the value of the item at the given index.
     * @param index the index to inspect, starting with 1
     * @return String the text value of the option at the given index
     */
	@Override
    public String getText(int index) {
    	return getOption(index).getAttribute("aria-label");
    }
    
    /**
     * Gets the text of the first item currently selected.
     * @return String the text value of the selected option
     */
	@Override
    public String getSelectedText() {
    	return this.element(By.xpath("//span[contains(@class, 'p-dropdown-label')]")).getText();
   }
}
