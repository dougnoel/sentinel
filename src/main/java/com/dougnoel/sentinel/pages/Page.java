package com.dougnoel.sentinel.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;

import com.dougnoel.sentinel.elements.Element;
import com.dougnoel.sentinel.elements.ElementFactory;
import com.dougnoel.sentinel.elements.tables.Table;
import com.dougnoel.sentinel.enums.SelectorType;
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

    protected Map<String,Element> elements;
    
    private String pageName;
    
    public Page(String pageName) {
    	this.pageName = pageName;
        elements = new HashMap<>();
    }

    public String getName() {
        return pageName;
    }

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
