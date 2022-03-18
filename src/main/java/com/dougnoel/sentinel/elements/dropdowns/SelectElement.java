package com.dougnoel.sentinel.elements.dropdowns;

import java.util.Map;

import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.elements.Element;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

/**
 * Extends Element. Is intended to be a a base class for Dropdown.
 */
public class SelectElement extends Element {

	/**
	 * Implementation of a Element to initialize how an element is going to be found when it is worked on by the 
	 * WebDriver class. Takes a reference to the WebDriver class that will be exercising its functionality.
	 * 
	 * @param elementName String the name of the element
	 * @param selectors Map&lt;String,String&gt; the list of selectors to use to find the element
	 */
	public SelectElement(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
	}

    /**
     * Selects an option from a drop down using the text value of the item to select.
     * @param selectText the value to select
     * @return PageSelectElement for object chaining
     */
    public SelectElement select(String selectText) {
        Select selectElement = new Select(this.element());
        selectElement.selectByVisibleText(selectText);

        return this;
    }

    /**
     * Selects an option from a drop down using the ordinal value of the item to select.
     * @param index the index to select
     * @return PageSelectElement for object chaining
     */
    public SelectElement select(int index){
        Select selectElement = new Select(this.element());
        selectElement.selectByIndex(index);
        
        return this;
    }

    /**
     * Selects an option from a drop down. Allows for selection by value in addition to 
     * selection by text or index.
     * @param selectorType com.dougnoel.sentinel.enums.SelectorType INDEX, VALUE, TEXT
     * @param selectText the value of the selector
     * @return PageSelectElement for object chaining
     */
    public SelectElement select(SelectorType selectorType, String selectText) {
        Select selectElement = new Select(this.element());
        switch (selectorType) {
        case INDEX:
            return select(Integer.parseInt(selectText));
        case VALUE:
            selectElement.selectByValue(selectText);
            break;
        case TEXT:
            return select(selectText);
        default:
            // This is here in case a new type is added to SelectorType and has not been
            // implemented yet here.
            String errorMessage = SentinelStringUtils.format(
                    "Unhandled selector type \"{}\" passed to Page Select Element class. Could not resolve the reference. Refer to the Javadoc for valid options.",
                    selectorType);
            throw new InvalidSelectorException(errorMessage);
        }

        return this;
    }

    /**
     * Gets the value of the item at the given index.
     * @param index the index to inspect
     * @return String the text value of the option at the given index
     */
    public String getText(int index){
        Select selectElement = new Select(this.element());
        return selectElement.getOptions().get(index).getText();
    }
    
    /**
     * Gets the text of the first item currently selected.
     * @return String the text value of the selected option
     */
    public String getSelectedText(){
        Select selectElement = new Select(this.element());
        return selectElement.getFirstSelectedOption().getText();
    }
    
    /**
     * Checks if the exact passed text is an available option in the select.
     * @return boolean true if any option of the select element has exactly matching text.
     */
    public boolean hasOption(String text) {
    	Select selectElement = new Select(this.element());
    	return new WebDriverWait(WebDriverFactory.getWebDriver(), Time.out().toSeconds(), Time.interval().toMillis())
				.ignoring(StaleElementReferenceException.class)
				.until(d -> selectElement.getOptions().stream().anyMatch(el -> el.getText().endsWith(text)));
    }
    
    /**
     * Checks if the exact passed text is an available option in the select.
     * @return boolean true if no option of the select element has exactly matching text.
     */
    public boolean doesNotHaveOption(String text) {
    	Select selectElement = new Select(this.element());
    	return new WebDriverWait(WebDriverFactory.getWebDriver(), Time.out().toSeconds(), Time.interval().toMillis())
				.ignoring(StaleElementReferenceException.class)
				.until(d -> selectElement.getOptions().stream().noneMatch(el -> el.getText().endsWith(text)));
    }
    
    /**
     * Returns the number of options in this Select element.
     * @return int the number of options in this Select element.
     */
    public int getNumberOfOptions() {
    	return new Select(this.element()).getOptions().size();
    }
}
