package com.dougnoel.sentinel.elements;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.time.Duration;
import java.util.EnumMap;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
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
import com.dougnoel.sentinel.exceptions.ElementNotVisibleException;
import com.dougnoel.sentinel.exceptions.MalformedSelectorException;
import com.dougnoel.sentinel.exceptions.NoSuchElementException;
import com.dougnoel.sentinel.exceptions.NoSuchSelectorException;
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
 * <b>Note:</b> Renamed from WebElement to PageElement to avoid name space
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
public class PageElement {
	private static final Logger log = LogManager.getLogger(PageElement.class.getName()); // Create a logger.

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
	public PageElement(String elementName, Map<String,String> selectors) {
		this("PageElement", elementName, selectors);
	}
	
	public PageElement(String elementType, String elementName, Map<String,String> selectors) {
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

	private WebElement getElementWithWait(final By locator, Duration timeout, Duration interval) {
		try {
		FluentWait<WebDriver> wait = new FluentWait<WebDriver>(driver)
			       .withTimeout(timeout)
			       .pollingEvery(interval)
			       .ignoring(org.openqa.selenium.NoSuchElementException.class);

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
		long startTime = System.currentTimeMillis(); //fetch starting time
		while((System.currentTimeMillis()-startTime) < Time.out() * 1000) {
    	    for (Map.Entry<SelectorType, String> selector : selectors.entrySet()) {
    	    	log.trace("Attempting to find {} {} with {}: {}", elementType, getName(), selector.getKey(), selector.getValue());
    	    	element = getElementWithWait(createByLocator(selector.getKey(), selector.getValue()), Duration.ofMillis(100), Duration.ofMillis(10));
    	    	if (element != null) {
    	    		return element;
    	    	}
    	    }
        }
		var errorMessage = SentinelStringUtils.format("{} element named \"{}\" does not exist or is not visible using the following values: {}. Assure you are on the page you think you are on, and that the element identifier you are using is correct.",
				elementType, getName(), selectors);
		throw new NoSuchElementException(errorMessage);
	}

	/**
	 * Type text into a PageElement.
	 * <p>
	 * <b>Aliases:</b>
	 * <ul>
	 * <li>Textbox.type(text)</li>
	 * </ul>
	 * 
	 * @param text
	 *            String (text to type)
	 * @return PageElement (for chaining)
	 */
	public PageElement sendKeys(String text) {
		element().click();
		element().clear();
		element().sendKeys(text);
		return this;
	}

	public PageElement javaScriptSendKeys(String text) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;
		jse.executeScript("arguments[0].value='" + text + "';", element());

		return this;
	}

	/**
	 * Press keys with focus on a PageElement. This is useful when type() or
	 * sendKeys isn't working due to a mask or hidden field being employed to grab
	 * key press events and operate on each one.
	 * 
	 * @param text
	 *            String (keys to type)
	 * @return PageElement (for chaining)
	 * @throws AWTException if the key cannot be pressed.
	 */
	public PageElement pressKeys(String text) throws AWTException {
		// Ensure that the element has focus.
		if ("input".equals(element().getTagName())) {
			element().sendKeys("");
		} else {
			new Actions(driver).moveToElement(element()).perform();
		}

		// Iterate through the string and press every key
		var robot = new Robot();
		robot.delay(1000);
		char[] chars = text.toCharArray();

		for (char c : chars) {
			log.debug(c);
			robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(c));
			robot.delay(1000);
			robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(c));
			robot.delay(1000);
		}

		return this;
	}

	/**
	 * Click a PageElement.
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
	 * @return PageElement (for chaining)
	 */
	public PageElement click() {
		long waitTime = Time.out();
		try {
			new WebDriverWait(driver, waitTime).until(ExpectedConditions.elementToBeClickable(element())).click();
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
	 * Clear a PageElement. Clears text in a text box. Un-checks check boxes. Clears
	 * radio button choices.
	 * <p>
	 * <b>Aliases:</b>
	 * <ul>
	 * <li>Checkbox.uncheck()</li>
	 * </ul>
	 * 
	 * @return PageElement (for chaining)
	 */
	public PageElement clear() {
		element().clear();
		return this;
	}

	/**
	 * Returns true if the element is enabled within 10 seconds; otherwise returns
	 * false.
	 * 
	 * @return boolean true if the element is enabled within 10 seconds; otherwise returns false.
	 */
	public boolean isEnabled()  {
		return isEnabled(10);
	}

	/**
	 * Returns true if the element is enabled within the number of seconds
	 * indicated; otherwise returns false.
	 * <p>
	 * This function waits a number of seconds in 500 millisecond increments to see
	 * if the element is visible. This wait ensures that context-switching, such as
	 * bringing up a pop-up, AJAX calls, etc. will not fail a test.
	 * <p>
	 * A StaleElementReferenceException can be thrown when testing a Bootstrap
	 * website that uses divs as popups. We resolve this by catching the exception
	 * and retrying it 5 times. If it still fails, we catch the exception and return
	 * a failure indicating the element wasn't found instead of throwing an
	 * exception.
	 * 
	 * @param seconds int the number of seconds to wait before returning failure.
	 * @return boolean true if the element is enabled within the number of seconds indicated; otherwise returns false.
	 */
	public boolean isEnabled(int seconds) {
		var retries = 0;
		while (true) {
			try {
				return new WebDriverWait(driver, seconds).until(ExpectedConditions.elementToBeClickable(element()))
						.isEnabled();
			} catch (StaleElementReferenceException e) {
				if (retries < 5) {
					retries++;
				} else {
					return false;
				}
			} catch (TimeoutException e) {
				return false;
			}
		}
	}

	/**
	 * Validates whether or not the element is selected.
	 * @return boolean true if the element is selected, false if it is not
	 */
	public boolean isSelected() {
		return element().isSelected();
	}

	/**
	 * Returns true if the element is displayed within 10 seconds; otherwise returns
	 * false.
	 * 
	 * @return boolean true if the element is displayed within 10 seconds; otherwise returns false.
	 */
	public boolean isDisplayed() {
		return isDisplayed(10);
	}

	/**
	 * Returns true if the element is displayed within the number of seconds
	 * indicated; otherwise returns false.
	 * <p>
	 * This function waits a number of seconds in 500 millisecond increments to see
	 * if the element is visible. This wait ensures that context-switching, such as
	 * bringing up a pop-up, AJAX calls, etc. will not fail a test.
	 * <p>
	 * A StaleElementReferenceException can be thrown when testing a Boostrap
	 * website that uses divs as popups. We resolve this by catching the exception
	 * and retrying it 5 times. If it still fails, we catch the exception and return
	 * a failure indicating the element wasn't found instead of throwing an
	 * exception.
	 * 
	 * @param seconds int the number of seconds to wait before returning failure.
	 * @return boolean true if the element is displayed within the number of seconds indicated; otherwise returns false.
	 */
	public boolean isDisplayed(int seconds) {
		var retries = 0;
		while (true) {
			try {
				return new WebDriverWait(driver, seconds).until(ExpectedConditions.visibilityOf(element()))
						.isDisplayed();
			} catch (StaleElementReferenceException e) {
				if (retries < 5) {
					retries++;
				} else {
					return false;
				}
			} catch (TimeoutException e) {
				return false;
			}
		}
	}

	/**
	 * TODO: Test to make sure this to work with multiple selectors
	 * Determines with 250 milliseconds (1/4 of a second) if an element is not present.
	 * This should be used when you expect an element to not be present and do not want
	 * to slow down your tests waiting for the normal timeout time to expire.
	 * @return boolean true if the element cannot be found, false if it is found
	 */
	public boolean doesNotExist() {
	    for (Map.Entry<SelectorType, String> selector : selectors.entrySet()) {
	    	log.trace("Expecting to not find with {} {}", selector.getKey(), selector.getValue());
	    	WebElement element = getElementWithWait(createByLocator(selector.getKey(), selector.getValue()), Duration.ofMillis(100), Duration.ofMillis(10));
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
	 * Returns the WebElement wrapped inside the PageElement so that it can be acted
	 * upon inside of step definitions.
	 * @return org.openqa.selenium.WebElement
	 */
	public WebElement toWebElement() {
		return element();
	}

	/**
	 * Verifies if the element has a class value.
	 * <p>
	 * <b>Examples:</b>
	 * <ul>
	 * <li>Determine if an element is highlighted because class="active"</li>
	 * </ul>
	 * 
	 * @param text String the class to verify
	 * @return boolean
	 */
	public boolean hasClass(String text) {
		String classes = element().getAttribute("class");
		log.debug("Classes found on element {}: {}", this.getClass().getName(), classes);
		for (String c : classes.split(" ")) {
			if (c.equals(text)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if the element as an attribute equal to the value passed;
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

		return false;
	}

}
