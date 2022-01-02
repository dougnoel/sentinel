package com.dougnoel.sentinel.pages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.elements.Element;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

/**
 * Page class to contain a URL and the elements on the page.
 * <p>
 * TO DO: Abstract out the driver creation to allow multiple drivers to be created
 * at once.
 */
public class Page {
	private static final Logger log = LogManager.getLogger(Page.class);
	
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
        return elements.computeIfAbsent(normalizedName, name -> ((Element)(createElement(name))));
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
	
	private Object createElement(String elementName) {
		Map<String, String> elementData = findElement(elementName, getName());
		
		if (elementData == null) {
			var errorMessage = SentinelStringUtils.format("Data for the element {} could not be found in the {}.yml file.", elementName, this.getName());
			throw new NoSuchElementException(errorMessage);
		}
		
		String elementType = null;
		if (elementData.containsKey("elementType")) {
			elementType = elementData.get("elementType");
		}
		else {
			elementType = "Element";
		}
		
		String fullClassFilePath = Configuration.getClassPath(elementType);

		try {
			return Class.forName(fullClassFilePath).getConstructor(String.class, Map.class).newInstance(elementName, elementData);
		} catch (Exception e) {
			var errorMessage = SentinelStringUtils.format("{}: {} Element Object creation failed. File location: {}", e.getClass().getSimpleName(), StringUtils.capitalize(elementName), fullClassFilePath);
			log.error(errorMessage);
			throw new NoSuchElementException(errorMessage, e);
		}

		 //TODO: This allows people to call their element type whatever they want without needing a child class to implement it.
//		 return new Element(elementType, elementName, elementData);
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
}
