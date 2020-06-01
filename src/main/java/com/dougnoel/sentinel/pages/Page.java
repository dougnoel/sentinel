package com.dougnoel.sentinel.pages;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.elements.PageElement;
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
        return elements.computeIfAbsent(normalizedName, name -> createPageElement(name));
	}
	
	private PageElement createPageElement(String elementName) {
		Map<String, String> elementData = ConfigurationManager.getElement(elementName, getName());
		
		String elementType = elementData.get("elementType");
		SelectorType selectorType = SelectorType.of(elementData.get("selectorType"));
		String locatorValue = elementData.get("locatorValue");
		//TODO: Use Reflection to do this
		if ("Table".equalsIgnoreCase(elementType)) {
			return new Table(selectorType, locatorValue);
		}
		return null;
	}
}
