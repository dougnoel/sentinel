package com.dougnoel.sentinel.pages;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.elements.Checkbox;
import com.dougnoel.sentinel.elements.PageElement;
import com.dougnoel.sentinel.elements.Textbox;
import com.dougnoel.sentinel.elements.dropdowns.Dropdown;
import com.dougnoel.sentinel.elements.dropdowns.MaterialUISelect;
import com.dougnoel.sentinel.elements.dropdowns.PrimeNGDropdown;
import com.dougnoel.sentinel.elements.dropdowns.SelectElement;
import com.dougnoel.sentinel.elements.radiobuttons.PrimeNGRadioButton;
import com.dougnoel.sentinel.elements.radiobuttons.Radiobutton;
import com.dougnoel.sentinel.elements.tables.NGXDataTable;
import com.dougnoel.sentinel.elements.tables.Table;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

/**
 * Page class to contain a URL and the elements on the page.
 * <p>
 * <b>TO DO:</b>
 * <ul>
 * <li>Turn this into an abstract class.</li>
 * <li>Create a PageFactory</li>
 * <li>Abstract out the driver creation to allow multiple drivers to be created
 * at once</li>
 * </ul>
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

    protected URL url;
    
    protected Map<String,PageElement> elements;
    
    private String pageName;

    /**
     * Initializes a WebDriver object for operating on page elements, and sets the
     * base URL for the page.
     */
    public Page() {
    	this.pageName = this.getClass().getSimpleName();
        driver = WebDriverFactory.getWebDriver();
        elements = new HashMap<>();
    }
    
    public Page(String pageName) {
    	this.pageName = pageName;
        driver = WebDriverFactory.getWebDriver();
        elements = new HashMap<>();
    }

    /**
     * Maximizes the browser window. Stores the current window size and position so
     * you can return to the existing settings.
     * 
     * TODO: Move this higher up and out of individual page objects.
     * 
     * @return Page - Returns a page object for chaining.
     */
    public Page maximizeWindow() {
        driver.manage().window().maximize();
        return this;
    }

    /**
     * Returns the title of the current web page we are on. Useful for debugging and
     * assertions.
     * 
     * @return String
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Returns the URL of the current web page we are on. Useful for debugging and
     * assertions.
     * 
     * @return String
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getName() {
        return pageName;
    }

	public PageElement getElement(String elementName) {
        String normalizedName = elementName.replaceAll("\\s+", "_").toLowerCase();
        return elements.computeIfAbsent(normalizedName, name -> createElement(name));
	}
	
	private PageElement createElement(String elementName) {
		Map<String, String> elementData = ConfigurationManager.getElement(elementName, getName());
		
		String elementType = elementData.get("elementType");
		elementData.remove("elementType");

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
		return new PageElement(elementName, elementData);
	}
}
