package com.dougnoel.sentinel.elements;

import java.io.IOException;
import java.time.Duration;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.filemanagers.FileManager;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

/**
 * Base element class that handles creation of element types and allows late
 * binding. It takes a selector type, a selector value and a driver, and uses
 * that to exercise Selenium WebDriver functionality. Classes that extend this
 * are wrappers to allow clearer coding conventions. For example a Button
 * element just allows the specific declaration of a button on a web page for
 * functionality.
 * <p>
 * Selenium already has a WebElement class, but using it requires you to be able
 * to see the elements on the page as soon as you declare the page object. This
 * doesn't work, since we want to define the page before run time. The solution
 * to the problem is late binding, or declaring the objects as they are used.
 * Every time we use an element on the page, we look for it at the time we are
 * using it.
 * <p>
 * <b>Note:</b> Renamed from WebElement to Element to avoid name space
 * conflicts with selenium's WebElement object when Selenium's WebElement type
 * was needed as a return type for the element() function.
 * <p>
 * <b>To Do:</b>
 * <ul>
 * <li>Remove the need to pass in a driver class instance for the creation of
 * every element and delay that binding to a singleton class that can be
 * changed, allowing the use of multiple drivers through a testing session.</li>
 * <li>Look into creating a Selenium WebElement member variable to store the
 * element after the first time it is used. This could increase the speed of
 * calling objects that get called often.</li>
 * </ul>
 */
public class Element {
	private static final Logger log = LogManager.getLogger(Element.class.getName()); // Create a logger.

	protected Map<SelectorType,String> selectors;
	protected String name;
	private final String elementType;
	private WebDriver driver() { return WebDriverFactory.getWebDriver(); }

	/**
	 * The constructor for a WebElement to initialize how an element is going to be
	 * found when it is worked on by the WebDriver class. Takes a reference to the
	 * WebDriver class that will be exercising its functionality.
	 * 
	 * @param elementName String the element name
	 * @param selectors Map the various selectors to iterate through to find the element
	 */
	public Element(String elementName, Map<String,String> selectors) {
		this("Element", elementName, selectors);
	}
	
	public Element(String elementType, String elementName, Map<String,String> selectors) {
		this.selectors = new EnumMap<>(SelectorType.class);
		selectors.forEach((locatorType, locatorValue) -> {
			if (!"elementType".equalsIgnoreCase(locatorType)) {
				try {
					this.selectors.put(SelectorType.of(locatorType), locatorValue);
				} catch (IllegalArgumentException e) {
					var errorMessage = SentinelStringUtils.format("{} is not a valid selector type. Please fix the element {} in the {}.yml page object.", locatorType, elementName, PageManager.getPage().getName());
					log.error(errorMessage);
					throw new InvalidSelectorException(errorMessage);
				}
			}
		});
		this.elementType = elementType;
		name = elementName;
	}
	
	/**
	 * Returns the name of the element as it is stored in the yaml file.
	 * @return String the name of the element
	 */
	public String getName() {
		return name;
	}

	/**
	 * Searches for an element using the given locator for the passed duration. It will look
	 * multiple times, as determined by the Time.interval() value which defaults to 10 milliseconds.
	 * 
	 * @param locator By Selenium By locator
	 * @param timeout Duration the amount of time we look for an element before returning failure
	 * @return org.openqa.selenium.WebElement the Selenium WebElement if found, otherwise null
	 */
	private WebElement getElementWithWait(final By locator, Duration timeout) {
		try {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver())
			       .withTimeout(timeout)
			       .pollingEvery(Time.interval())
			       .ignoring(org.openqa.selenium.NoSuchElementException.class, StaleElementReferenceException.class);

		return wait.until(d -> driver().findElement(locator));
		}
		catch (org.openqa.selenium.TimeoutException e) {
			return null;
		}
	}
	
	/**
	 * Takes a Sentinel SelectorType and string value and returns a Selenium By locator.
	 * 
	 * @param selectorType SelectorType the ENUM value indicating how we search for the element
	 * @param selectorValue String the value being pulled from the config file for the selector
	 * @return org.openqa.selenium.By returns a Selenium By selector for locating an element.
	 */
	private By createByLocator(SelectorType selectorType, String selectorValue) {
		try {
			switch (selectorType) {
			case CLASS:
				return By.className(selectorValue);
			case CSS:
				return By.cssSelector(selectorValue);
			case ID:
				return By.id(selectorValue);
			case NAME:
				return By.name(selectorValue);
			case PARTIALTEXT:
				return By.partialLinkText(selectorValue);
			case TEXT:
				return By.linkText(selectorValue);
			case XPATH:
				return By.xpath(selectorValue);
			default:
				var errorMessage = SentinelStringUtils.format("{} is not a valid selector type. Please fix the element {} in the {}.yml page object.", selectorType, getName(), PageManager.getPage().getName());
				log.error(errorMessage);
				throw new InvalidSelectorException(errorMessage);
			}
		} catch (IllegalArgumentException e) {
			var errorMessage = SentinelStringUtils.format("{}: {} is not a valid selector. Fix the element {} in the {}.yml page object.", selectorType, selectorValue, getName(), PageManager.getPage().getName());
			log.error(errorMessage);
			throw new InvalidSelectorException(errorMessage, e);
		}
	}
	
	/**
	 * Returns the Selenium WebElement if it can be found on the current page.
	 * Provides late binding for elements so that the driver does not look for them
	 * until they are called, at which point the driver should be on the correct
	 * page.
	 * 
	 * @return org.openqa.selenium.WebElement the Selenium WebElement object type that can be acted upon
	 */
	protected WebElement element() {
		driver().switchTo().defaultContent();
		WebElement element = null;
		long searchTime = Time.out().getSeconds() * 1000;
		long startTime = System.currentTimeMillis(); //fetch starting time
		while((System.currentTimeMillis() - startTime) < searchTime) {
			element = findElementInCurrentFrameForDuration(Time.loopInterval());
	    	if (element != null) {
	    		return element;
	    	}
	    	element = findElementInIFrame();
	    	if (element != null) {
	    		return element;
	    	}	
        }
		var errorMessage = SentinelStringUtils.format("{} element named \"{}\" does not exist or is not visible using the following values: {}. Assure you are on the page you think you are on, and that the element identifier you are using is correct.",
				elementType, getName(), selectors);
		throw new NoSuchElementException(errorMessage);
	}
	
	/**
	 * Returns a Selenium WebElement using the current element as the beginning search point
	 * for the passed By locator.
	 * 
	 * @param locator org.openqa.selenium.By the locator to use to find the element inside of this one
	 * @return org.openqa.selenium.WebElement the Selenium WebElement object type that can be acted upon
	 */
	protected WebElement element(final By locator) {    	
		try {
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver())
					.withTimeout(Time.out())
					.pollingEvery(Time.interval())
					.ignoring(org.openqa.selenium.NoSuchElementException.class, StaleElementReferenceException.class);

			return wait.until(d -> element().findElement(locator));
		} catch (org.openqa.selenium.TimeoutException e) {
			return null;
		}
	}
	
	/**
	 * Searches recursively through any iFrames on the page for the element. Returns
	 * null if the element is not found, or if there are no iFrames on the page. This
	 * method traverses through iFrames but returns to the default root context upon
	 * returning.
	 * 
	 * @return WebElement the element if it is found, otherwise null
	 */
	protected WebElement findElementInIFrame() {
    	if (PageManager.getPage().hasIFrames()) {
    		WebElement element = null;
    		List <WebElement> iframes = PageManager.getPage().getIFrames();
    		try {
    			for (WebElement iframe : iframes) {
        			driver().switchTo().frame(iframe);
        			element = findElementInCurrentFrameForDuration(Time.loopInterval());
        			if (element != null) {
        				return element;
        			}
            	    element = findElementInIFrame();
        			if (element != null)
        				return element;
        			driver().switchTo().parentFrame();
        		}
    		}
    		catch(StaleElementReferenceException | NoSuchFrameException e) {
    			var errorMessage = SentinelStringUtils.format("Error when searching for {} element named \"{}\" while attempting to search through iFrames. Looping again. Error: {}",
    					elementType, getName(), e);
    			log.trace(errorMessage);
    			return null;
    		}
    		
    	}
    	return null;
	}

	/**
	 * Searches for the current element within the current frame context. Searches each selector for the passed
	 * amount of time as a Duration object. Recommended to be 100 milliseconds.
	 * 
	 * @param duration Duration total time to search for the element per selector
	 * @return WebElement the element if it is found, otherwise null
	 */
	private WebElement findElementInCurrentFrameForDuration(Duration duration) {
		WebElement element = null;
		for (Map.Entry<SelectorType, String> selector : selectors.entrySet()) {
			log.trace("Attempting to find {} {} with {}: {}", elementType, getName(), selector.getKey(), selector.getValue());
			element = getElementWithWait(createByLocator(selector.getKey(), selector.getValue()), duration);
			if (element != null) {
				return element;
			}
		}
		return null;
	}
	
	/**
	 * Enter text into a Element. Typically used for text boxes.
	 * This method will throw an ElementDisabledException() if the text box is disabled.
	 * 
	 * @param text String the text to enter
	 * @return Element (for chaining)
	 */
	public Element sendKeys(String text) {
		long searchTime = Time.out().getSeconds() * 1000;
		long startTime = System.currentTimeMillis(); //fetch starting time
		
		while((System.currentTimeMillis() - startTime) < searchTime) {
			if (sendKeysLoop(text))
				return this;
		}
		
		var errorMessage = SentinelStringUtils.format(
			"{} element named \"{}\" cannot receive text. Make sure the element is on the page. The selectors being used are: {} The total attempt time was {} seconds."
				, elementType
				, getName()
				, selectors
				, Time.out().getSeconds());
		if (!element().isEnabled()) {
			errorMessage += "\nElement is disabled. Please make sure the element is enabled to send text.";
		}
		log.error(errorMessage);
		throw new ElementNotVisibleException(errorMessage);
	}
	
	/**
	 * Loops through all the ways to send text to an element.
	 * 
	 * @param text String the text to send
	 * @return boolean true if the text was sent, false otherwise
	 */
	private boolean sendKeysLoop(String text) {
		WebElement element = element();
		try {
			this.click().clear();
			element.sendKeys(text);

			constructElementWait(Time.loopInterval())
					.until(ExpectedConditions.textToBePresentInElementValue(element, text));

			return true;
		} 
		catch (WebDriverException e) {
			if(!element().isEnabled()) { // Respect if an element is disabled.
				var errorMessage = SentinelStringUtils.format(
						"{} element named \"{}\" is disabled and cannot receive text. The selectors being used are: {}"
							, elementType
							, getName()
							, selectors);
				throw new ElementNotInteractableException(errorMessage);
		}
			try {
				JavascriptExecutor jse = (JavascriptExecutor) driver();
				jse.executeScript("arguments[0].value='" + text + "';", element());

				element = element();
				constructElementWait(Time.loopInterval())
						.until(ExpectedConditions.textToBePresentInElementValue(element, text));

				return true;
			} catch (WebDriverException e1) {
				return false;
			}
		}
	}
	
	/**
	 * Click an Element.
	 * <p>
	 * This function waits up to 10 seconds in 500 millisecond increments to see if
	 * the element is visible. This wait ensures that context-switching, such as
	 * bringing up a pop-up, AJAX calls, etc. will not fail a test.
	 * <p>
	 * <b>Aliases:</b>
	 * <ul>
	 * <li>Checkbox.check()</li>
	 * <li>Radiobutton.select()</li>
	 * </ul>
	 * 
	 * @return Element (for chaining)
	 */
	public Element click() {
		long searchTime = Time.out().getSeconds() * 1000;
		long startTime = System.currentTimeMillis(); //fetch starting time
		
		while((System.currentTimeMillis() - startTime) < searchTime) {
			if (clickLoop())
				return this;
		}
		WebElement element = element();
		var errorMessage = SentinelStringUtils.format(
			"{} element named \"{}\" cannot be clicked. Make sure the element is visible on the page when you attempt to click it. The selectors being used are: {} The total attempt time for all click types was {} seconds.",
				elementType
				, getName()
				, selectors
				, Time.out().getSeconds());
		if (element == null) {
			errorMessage += "\nElement does not exist.";
		}
		else if (!element.isEnabled()) {
				errorMessage += "\nElement is disabled.";
				log.error(errorMessage);
				throw new ElementNotInteractableException(errorMessage);
		}
		log.error(errorMessage);
		throw new ElementNotInteractableException(errorMessage);
	}

	/**
	 * Loops through all the ways to click an element.
	 * 
	 * @return boolean true if the element was clicked, false otherwise
	 */
	private boolean clickLoop() {
		WebElement element = element();
		try {
			constructElementWait(Time.loopInterval())
				.until(ExpectedConditions.elementToBeClickable(element))
				.click();
			return true;
		} catch (WebDriverException e) {
			try{
				hover();
				constructElementWait(Time.loopInterval())
					.until(ExpectedConditions.elementToBeClickable(element))
					.click();
				return true;
			} catch(WebDriverException e1){
				if (element.isEnabled()) {
					JavascriptExecutor executor = (JavascriptExecutor) driver();
					executor.executeScript("arguments[0].click();", element);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Creates a FluentWait Webdriver object for searching for elements.
	 * @param timeout Duration how long to search
	 * @return FluentWait the wait object that can be invoked
	 */
	private FluentWait<WebDriver> constructElementWait(Duration timeout) {
		return new FluentWait<WebDriver>(driver())
			       .withTimeout(timeout)
			       .pollingEvery(Time.interval())
			       .ignoring(org.openqa.selenium.NoSuchElementException.class, StaleElementReferenceException.class);
	}
	
	/**
	 * Clear a Element. Clears text in a text box. Un-checks check boxes. Clears
	 * radio button choices.
	 * <p>
	 * <b>Aliases:</b>
	 * <ul>
	 * <li>Checkbox.uncheck()</li>
	 * </ul>
	 * 
	 * @return Element (for chaining)
	 */
	public Element clear() {
		element().clear();
		return this;
	}

	/**
	 * Drags the current element on top of the target element.
	 * @param target Element the element the target is being dragged and dropped onto
	 * @return Element (for chaining)
	 * @throws IOException if the drag and drop javascript file cannot be loaded
	 */
	public Element dragAndDrop(Element target) throws IOException {
	    String script = FileManager.loadJavascript("src/main/resources/scripts/DragDrop.js");
	    
	    JavascriptExecutor executor = (JavascriptExecutor)WebDriverFactory.getWebDriver();
	    executor.executeScript(script, this.element(), target.element());
	    return this;	      
	}	  

	/**
	 * Returns true if the element is displayed, otherwise returns false if it is
	 * hidden/invisible.
	 * <p>
	 * NOTE: Use isInvisible() for the fastest processing time if you expect
	 * the element to be hidden/invisible.
	 * 
	 * @return boolean true if the element is displayed; false if it is hidden.
	 */
	public boolean isDisplayed() {
		try {
			return new WebDriverWait(driver(), Time.out(), Time.interval())
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.visibilityOf(element())).isDisplayed();
		}
		catch (TimeoutException e) {
			return false;
		}
	}

	/**
	 * Returns true if the element is hidden, otherwise returns false if it is
	 * visible/displayed.
	 * <p>
	 * NOTE: Use isDisplayed() for the fastest processing time if you expect
	 * the element to be visible/displayed.
	 * 
	 * @return boolean true if the element is hidden; false if it is displayed.
	 */
	public boolean isHidden() {
		try {
			return new WebDriverWait(driver(), Time.out(), Time.interval())
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.invisibilityOf(element()));
		}
		catch (TimeoutException e) {
			return false;
		}
	}
	
	/**
	 * Returns true if the element is enabled; false if it is disabled.
	 * Expects the element to be enabled, and if it is not, this method
	 * will check every 10 milliseconds until it is up to the configured
	 * timeout time (10 second default).
	 * <p>
	 * NOTE: Use isDisabled() for the fastest processing time if you expect
	 * the element to be disabled.
	 * 
	 * @return boolean true if the element is enabled; false if it is disabled
	 */
	public boolean isEnabled() {
		try {
			return new WebDriverWait(driver(), Time.out(), Time.interval())
				.ignoring(StaleElementReferenceException.class)
				.until(d -> element().isEnabled());
		}
		catch (TimeoutException e) {
			return false;
		}
	}

	/**
	 * Returns true if the element is disabled; false if it is enabled.
	 * Expects the element to be disabled, and if it is not, this method
	 * will check every 10 milliseconds until it is up to the configured
	 * timeout time (10 second default).
	 * <p>
	 * NOTE: Use isEnabled() for the fastest processing time if you expect
	 * the element to be enabled.
	 * 
	 * @return boolean true if the element is disabled; false if it is enabled
	 */
	public boolean isDisabled() {
		try {
			return new WebDriverWait(driver(), Time.out(), Time.interval())
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.attributeContains(element(), "disabled", ""));
		}
		catch (TimeoutException e) {
			return false;
		}
	}

	/**
	 * Returns true if the element is selected; false if it is not.
	 * <p>
	 * NOTE: Use isNotSelected() for the fastest processing time if you expect
	 * the element to not be selected.
	 * 
	 * @return boolean true if the element is selected, false if it is not
	 */
	public boolean isSelected() {
		try {
			return new WebDriverWait(driver(), Time.out(), Time.interval())
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.elementToBeSelected(element()));
		}
		catch (TimeoutException e) {
			return false;
		}
	}

	/**
	 * Returns true if the element is not selected; false if it is.
	 * <p>
	 * NOTE: Use isSelected() for the fastest processing time if you expect
	 * the element to be selected.
	 * 
	 * @return boolean true if the element is not selected, false if it is
	 */
	public boolean isNotSelected() {
		try {
			return new WebDriverWait(driver(), Time.out(), Time.interval())
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.elementSelectionStateToBe(element(), false));
		}
		catch (TimeoutException e) {
			return false;
		}
	}
	
	/**
	 * Determines with 100 milliseconds (1/10th of a second) if an element is not present.
	 * This should be used when you expect an element to not be present and do not want
	 * to slow down your tests waiting for the normal timeout time to expire.
	 * @return boolean true if the element cannot be found, false if it is found
	 */
	public boolean doesNotExist() {
	    for (Map.Entry<SelectorType, String> selector : selectors.entrySet()) {
	    	log.trace("Expecting to not find with {} {}", selector.getKey(), selector.getValue());
	    	WebElement element = getElementWithWait(createByLocator(selector.getKey(), selector.getValue()), Duration.ofMillis(100));
	    	if (element == null || !(element.isDisplayed())) {
	    		log.trace("doesNotExist() return result: true");
	    		return true;
	    	}
	    }
	    log.trace("doesNotExist() return result: false");
	    return false;
	}

	/**
	 * Returns the text of the page element as a String.
	 * 
	 * @return String The text value stored in the element.	 */
	public String getText() {
		return element().getText();
	}

	/**
	 * Returns true if the attribute exists for the element; otherwise returns false.
	 * Expects the attribute to exist, and if it does not, this method will check every 
	 * 10 milliseconds up until to the configured timeout time (10 second default).
	 * <p>
	 * NOTE: Use doesNotHaveAttribute() for the fastest processing time if you expect the
	 * element to not have the attribute.
	 * 
	 * @param attribute String the attribute for which to test
	 * @return true if the attribute exists for the element; otherwise false
	 */
	public boolean hasAttribute(String attribute) {
		return new WebDriverWait(driver(), Time.out(), Time.interval())
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.attributeToBeNotEmpty(element(), attribute));
	}

	/**
	 * Returns true if the attribute does not exist for the element; otherwise returns false.
	 * Expects the attribute to not exist, and if it does, this method will check every 
	 * 10 milliseconds up until to the configured timeout time (10 second default).
	 * <p>
	 * NOTE: Use hasAttribute() for the fastest processing time if you expect the element
	 * to have the attribute.
	 * 
	 * @param attribute String the attribute for which to test
	 * @return true if the attribute does not exist for the element; otherwise false
	 */
	public boolean doesNotHaveAttribute(String attribute) {
		return new WebDriverWait(driver(), Time.out(), Time.interval())
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.not(
						ExpectedConditions.attributeToBeNotEmpty(element(), attribute)));
	}
	
	/**
	 * Returns true if the element has an attribute equal to the value passed;
	 * otherwise returns false.
	 * <p>
	 * <b>Examples:</b>
	 * <ul>
	 * <li>Determine if an element has a style="display:none" attribute set.
	 * </ul>
	 * 
	 * @param attribute String the attribute to look for
	 * @param value String the value to which attribute should be set
	 * @return boolean true if the element as an attribute equal to the value passed; otherwise returns false
	 */
	public boolean attributeEquals(String attribute, String value) {
		if (hasAttribute(attribute)) {
			String values = element().getAttribute(attribute);
			log.debug("Values found for attribute {} on element {}: {}", attribute, this.getClass().getName(),
					values);
			if (values.equals(value)) {
				return true;
			} else {
				for (String c : values.split(" ")) {
					if (c.equals(value)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Hovers over an element using Actions.
	 * @return Element for chaining
	 */
	public Element hover() {
		new Actions(driver()).moveToElement(element()).build().perform();
		return this;
	}
	
	/**
	 * This method is used to get the text on mouse hover
	 * 
	 * @return The value of the tooltip text
	 */
	public String getTooltipText() {
		hover();
		return driver().findElement(By.xpath("//*[contains(text(),'')]")).getText();
	}	
	
}