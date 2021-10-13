package com.dougnoel.sentinel.pages;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.elements.Checkbox;
import com.dougnoel.sentinel.elements.Element;
import com.dougnoel.sentinel.elements.Textbox;
import com.dougnoel.sentinel.elements.dropdowns.Dropdown;
import com.dougnoel.sentinel.elements.dropdowns.MaterialUISelect;
import com.dougnoel.sentinel.elements.dropdowns.MetabolonPortalCheckboxDropdown;
import com.dougnoel.sentinel.elements.dropdowns.MetabolonPortalDropdown;
import com.dougnoel.sentinel.elements.dropdowns.PrimeNGDropdown;
import com.dougnoel.sentinel.elements.dropdowns.SelectElement;
import com.dougnoel.sentinel.elements.radiobuttons.PrimeNGRadioButton;
import com.dougnoel.sentinel.elements.radiobuttons.Radiobutton;
import com.dougnoel.sentinel.elements.tables.NGXDataTable;
import com.dougnoel.sentinel.elements.tables.Table;
import com.dougnoel.sentinel.elements.tables.MetabolonPortalResultsTable;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.exceptions.ElementNotFoundException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

/**
 * Page class to contain a URL and the elements on the page.
 * <p>
 * TO DO: Abstract out the driver creation to allow multiple drivers to be created
 * at once.
 */
public class Page {
	
	protected static final SelectorType CLASS = SelectorType.CLASS;
	protected static final SelectorType CSS = SelectorType.CSS;
	protected static final SelectorType ID = SelectorType.ID;
	protected static final SelectorType NAME = SelectorType.NAME;
	protected static final SelectorType PARTIALTEXT = SelectorType.PARTIALTEXT;
	protected static final SelectorType TEXT = SelectorType.TEXT;
	protected static final SelectorType XPATH = SelectorType.XPATH;    

    protected WebDriver driver;

    protected Map<String,Element> elements;
    
    private String pageName;
    
    public Page(String pageName) {
    	this.pageName = pageName;
        driver = WebDriverFactory.getWebDriver();
        elements = new HashMap<>();
    }

    public String getName() {
        return pageName;
    }

	public Element getElement(String elementName) {
        String normalizedName = elementName.replaceAll("\\s+", "_").toLowerCase();
        return elements.computeIfAbsent(normalizedName, name -> createElement(name));
	}
	
	private Map<String, String> findElement(String elementName, String pageName) {
		Map<String, String> elementData = Configuration.getElement(elementName, pageName);
		if (elementData == null) {
			for (String page : Configuration.getPageParts(pageName)) {
				elementData = findElement(elementName, page);
				if (elementData != null) {
					break;
				}
			}
		}
		return elementData;
	}
	
	private Element createElement(String elementName) {
		Map<String, String> elementData = findElement(elementName, getName());
		
		if (elementData == null) {
			var errorMessage = SentinelStringUtils.format("Data for the element {} could not be found in the {}.yml file.", elementName, this.getName());
			throw new ElementNotFoundException(errorMessage);
		}
		
		String elementType = null;
		if (elementData.containsKey("elementType")) {
			elementType = elementData.get("elementType");
		}
		else {
			elementType = "Element";
		}

		if ("Checkbox".equalsIgnoreCase(elementType)) {
			return new Checkbox(elementName, elementData);
		}
		if ("Textbox".equalsIgnoreCase(elementType)) {
			return new Textbox(elementName, elementData);
		}
		if ("Dropdown".equalsIgnoreCase(elementType)) {
			return new Dropdown(elementName, elementData);
		}
		if ("MaterialUISelect".equalsIgnoreCase(elementType)) {
			return new MaterialUISelect(elementName, elementData);
		}
		if ("PrimeNGDropdown".equalsIgnoreCase(elementType)) {
			return new PrimeNGDropdown(elementName, elementData);
		}
		if ("MetabolonPortalDropdown".equalsIgnoreCase(elementType)) {
			return new MetabolonPortalDropdown(elementName, elementData);
		}
		if ("MetabolonPortalCheckboxDropdown".equalsIgnoreCase(elementType)) {
			return new MetabolonPortalCheckboxDropdown(elementName, elementData);
		}
		if ("SelectElement".equalsIgnoreCase(elementType)) {
			return new SelectElement(elementName, elementData);
		}
		if ("PrimeNGRadioButton".equalsIgnoreCase(elementType)) {
			return new PrimeNGRadioButton(elementName, elementData);
		}
		if ("Radiobutton".equalsIgnoreCase(elementType)) {
			return new Radiobutton(elementName, elementData);
		}
		if ("NGXDataTable".equalsIgnoreCase(elementType)) {
			return new NGXDataTable(elementName, elementData);
		}
		if ("Table".equalsIgnoreCase(elementType)) {
			return new Table(elementName, elementData);
		}
		if ("MetabolonPortalResultsTable".equalsIgnoreCase(elementType)) {
			return new MetabolonPortalResultsTable(elementName, elementData);
		}
		// This allows people to call their element type whatever they want without needing a child class to implement it.
		return new Element(elementType, elementName, elementData);
	}
}
