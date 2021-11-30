package com.dougnoel.sentinel.elements;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.Duration;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.exceptions.ElementNotVisibleException;
import com.dougnoel.sentinel.exceptions.MalformedSelectorException;
import com.dougnoel.sentinel.exceptions.NoSuchElementException;
import com.dougnoel.sentinel.exceptions.NoSuchSelectorException;
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
	protected WebDriver driver;

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
					throw new NoSuchSelectorException(errorMessage);
				}
			}
		});
		this.elementType = elementType;
		name = elementName;
		this.driver = WebDriverFactory.getWebDriver();
	}
	
	/**
	 * Returns the name of the element as it is stored in the yaml file.
	 * @return String the name of the element
	 */
	public String getName() {
		return name;
	}

	private WebElement getElementWithWait(final By locator, Duration timeout) {
		try {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			       .withTimeout(timeout)
			       .pollingEvery(Time.interval())
			       .ignoring(org.openqa.selenium.NoSuchElementException.class, StaleElementReferenceException.class);

		return wait.until(d -> driver.findElement(locator));
		}
		catch (org.openqa.selenium.TimeoutException e) {
			return null;
		}
	}
	
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
				throw new NoSuchSelectorException(errorMessage);
			}
		} catch (IllegalArgumentException e) {
			var errorMessage = SentinelStringUtils.format("{}: {} is not a valid selector. Fix the element {} in the {}.yml page object.", selectorType, selectorValue, getName(), PageManager.getPage().getName());
			log.error(errorMessage);
			throw new MalformedSelectorException(errorMessage, e);
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
		WebElement element = null;
		long searchTime = Time.out().getSeconds() * 1000;
		long startTime = System.currentTimeMillis(); //fetch starting time
		while((System.currentTimeMillis() - startTime) < searchTime) {
			element = findElementInCurrentFrameForDuration(Duration.ofMillis(100));
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
			FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
					.withTimeout(Time.out())
					.pollingEvery(Time.interval())
					.ignoring(org.openqa.selenium.NoSuchElementException.class, StaleElementReferenceException.class);

			WebElement element = wait.until(d -> driver.findElement(locator));
			return wait.until(d -> element.findElement(locator));
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
    		for (WebElement iframe : iframes) {
    			driver.switchTo().frame(iframe);
    			element = findElementInCurrentFrameForDuration(Duration.ofMillis(100));
    			if (element != null) {
    				driver.switchTo().defaultContent();
    				return element;
    			}
        	    element = findElementInIFrame();
    			if (element != null)
    				return element;
    			driver.switchTo().parentFrame();
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
	 * Type text into a Element.
	 * <p>
	 * <b>Aliases:</b>
	 * <ul>
	 * <li>Textbox.type(text)</li>
	 * </ul>
	 * 
	 * @param text
	 *            String (text to type)
	 * @return Element (for chaining)
	 */
	public Element sendKeys(String text) {
		this.click().clear();
		element().sendKeys(text);
		return this;
	}

	public Element javaScriptSendKeys(String text) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].value='" + text + "';", element());

		return this;
	}

	/**
	 * Press keys with focus on a Element. This is useful when type() or
	 * sendKeys isn't working due to a mask or hidden field being employed to grab
	 * key press events and operate on each one.
	 * 
	 * @param text
	 *            String (keys to type)
	 * @return Element (for chaining)
	 * @throws AWTException if the key cannot be pressed.
	 */
	public Element pressKeys(String text) throws AWTException {
		// Ensure that the element has focus.
		if ("input".equals(element().getTagName())) {
			element().sendKeys("");
		} else {
			new Actions(driver).moveToElement(element()).perform();
		}

		// Iterate through the string and press every key
		var robot = new Robot();
		robot.setAutoWaitForIdle(true);
		robot.delay(150);
		robot.waitForIdle();
		
		char[] chars = text.toCharArray();
        
		for (char c : chars) {
			log.trace(c);
			robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
			robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(c));
		}

		return this;
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
		long waitTime = Time.out().getSeconds();
		try {
			new WebDriverWait(driver, waitTime, Time.interval().toMillis())
			.until(ExpectedConditions.elementToBeClickable(element()))
			.click();
		} catch (WebDriverException e) {
			try {
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", element());
			} catch (Exception e2) {
				var errorMessage = SentinelStringUtils.format(
						"{} element named \"{}\" does not exist or is not visible using the following values: {}. It cannot be clicked. Make sure the element is visible on the page when you attempt to click it. Clicking was attempted once with a mouse click and once with the Return key. The total wait time was {} seconds.",
								elementType, getName(), selectors, waitTime);
				log.error(errorMessage);
				throw new ElementNotVisibleException(errorMessage, e2);
			}
		}
		return this;
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
		return new WebDriverWait(driver, Time.out().toSeconds(), Time.interval().toMillis())
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.visibilityOf(element())).isDisplayed();
	}

	/**
	 * Returns true if the element is invisible, otherwise returns false if it is
	 * visible/displayed.
	 * <p>
	 * NOTE: Use isDisplayed() for the fastest processing time if you expect
	 * the element to be visible/displayed.
	 * 
	 * @return boolean true if the element is invisible; false if it is displayed.
	 */
	public boolean isInvisible() {
		return new WebDriverWait(driver, Time.out().toSeconds(), Time.interval().toMillis())
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.invisibilityOf(element()));
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
		return new WebDriverWait(driver, Time.out().toSeconds(), Time.interval().toMillis())
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.not(
						ExpectedConditions.attributeContains(element(), "disabled", "")));
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
		return new WebDriverWait(driver, Time.out().toSeconds(), Time.interval().toMillis())
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.attributeContains(element(), "disabled", ""));
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
		return new WebDriverWait(driver, Time.out().toSeconds(), Time.interval().toMillis())
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.elementToBeSelected(element()));
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
		return new WebDriverWait(driver, Time.out().toSeconds(), Time.interval().toMillis())
				.ignoring(StaleElementReferenceException.class)
				.until(ExpectedConditions.elementSelectionStateToBe(element(), false));
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
		return new WebDriverWait(driver, Time.out().toSeconds(), Time.interval().toMillis())
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
		return new WebDriverWait(driver, Time.out().toSeconds(), Time.interval().toMillis())
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
	 * This method is used to get the text on mouse hover
	 * 
	 * @return The value of the tooltip text
	 */
	public String getTooltipText() {
		new Actions(driver).moveToElement(this.element()).build().perform();
		return driver.findElement(By.xpath("//*[contains(text(),'')]")).getText();
	}	
	
}