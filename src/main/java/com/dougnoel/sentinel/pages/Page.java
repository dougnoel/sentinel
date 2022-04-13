package com.dougnoel.sentinel.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.elements.Element;
import com.dougnoel.sentinel.elements.ElementFactory;
import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.elements.tables.Table;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

/**
 * Page class to contain the details of an page.
 */
public class Page {
	
	protected static final SelectorType CLASS = SelectorType.CLASS;
	protected static final SelectorType CSS = SelectorType.CSS;
	protected static final SelectorType ID = SelectorType.ID;
	protected static final SelectorType NAME = SelectorType.NAME;
	protected static final SelectorType PARTIALTEXT = SelectorType.PARTIALTEXT;
	protected static final SelectorType TEXT = SelectorType.TEXT;
	protected static final SelectorType XPATH = SelectorType.XPATH;
	protected static final PageObjectType EXECUTABLE = PageObjectType.EXECUTABLE;

    protected Map<String,Element> elements;
    
    private String pageName;
    private PageObjectType pageType = null;
    
    /**
     * Constructor
     * @param pageName String the exact name of the page as stored on disk without extension.
     */
    public Page(String pageName) {
    	this.pageName = pageName;
        elements = new HashMap<>();
    }

    /**
     * Returns the name of the page without the .yml extension but otherwise
     * exactly matching the page object name on file. Since a page object
     * cannot be created without a valid page object yaml file, this value will
     * never be null or empty. The page name will also not have any spaces in it
     * as those are always stripped before creation.
     * 
     * @return String the name of the page object
     */
    public String getName() {
        return pageName;
    }

    /**
     * Returns an Element if it exists in the page object yaml file. If the element
     * name contains spaces they are replaced by underscores, and all characters
     * are made lowercase. The element is created if it has not been loaded yet, 
     * but otherwise is pulled from memory. In this way we only dynamically load 
     * the elements used and do not try to process an entire page object file at once.
     * <p>
     * Note that Element object itself handles finding the element on the page and
     * that is always handled dynamically.
     *  
     * @param elementName String the name of the element to find
     * @return com.dougnoel.sentinel.Element the Element object found 
     */
	public Element getElement(String elementName) {
        String normalizedName = elementName.replaceAll("\\s+", "_").toLowerCase();
        return elements.computeIfAbsent(normalizedName, name -> ((Element)(ElementFactory.createElement(name, this))));
    }
	
	/**
	 * Clears the cached Table objects for this page. 
	 * This action prevents StaleElementReferenceException when a table is referenced after previous navigation.
	 */
	public void clearTables() {
		elements = elements.entrySet().stream()
				.filter(
						entry -> !(entry.getValue() instanceof Table))
				.collect(
						Collectors.toMap(Entry::getKey, 
								Entry::getValue));
	}
	
	/**
	 * Returns true if iFrames exist on the page, false if they do not.
	 * 
	 * @return true if iFrames exist on the page, false if they do not
	 */
	public boolean hasIFrames() {
		return !WebDriverFactory.getWebDriver().findElements(By.xpath("//iframe")).isEmpty();
	}
	
	/**
	 * Returns a list of WebElement objects containing all the iFrames on the page.
	 * 
	 * @return List &lt;WebElement&gt; the list of iFrames in this page
	 */
	public List <WebElement> getIFrames() {
		return WebDriverFactory.getWebDriver().findElements(By.xpath("//iframe"));
	}
	
	/**
	 * Returns either WEBPAGE or EXECUTABLE based on the type of page object this is.
	 * 
	 * @return PageObjectType the type of page object (WEBPAGE, EXECUTABLE, UNKNOWN)
	 */
	public PageObjectType getPageObjectType() {
		
		if (pageType == null) {
			pageType = Configuration.getPageObjectType(pageName);
		}
		return pageType;
	}

	/**
	 * Returns true if a Javascript alert is present. False if an alert is not found. Driver returns back to the previous window's context if the alert is found.
	 * @return boolean true if an alert is present, false otherwise.
	 */
	public boolean isJsAlertPresent() {
		var driver = WebDriverFactory.getWebDriver();
        try
        {
            String currentWindow = driver.getWindowHandle();
            driver.switchTo().alert();
            driver.switchTo().window(currentWindow);
            return true;
        }
        catch (NoAlertPresentException e)
        {
            return false;
        }
	}
	
	/**
	 * Gets the text on the JS alert.
	 * 
	 * @return String the text in the JS alert
	 * @throws NoAlertPresentException if no alert is present.
	 */
	public String getJsAlertText() throws NoAlertPresentException {
		var driver = WebDriverFactory.getWebDriver();
		String text = "";
		String currentWindow = driver.getWindowHandle();
		text = driver.switchTo().alert().getText();
		driver.switchTo().window(currentWindow);
		return text;
	}
}
