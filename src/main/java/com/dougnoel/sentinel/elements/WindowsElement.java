package com.dougnoel.sentinel.elements;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Map;

import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.system.FileManager;
import com.dougnoel.sentinel.webdrivers.Driver;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.Color;

import io.appium.java_client.MobileBy;

/**
 * WinAppDriver implementation of a Element.
 */
public class WindowsElement extends Element {

	/**
	 * Default Windows Element constructor. An override of the Element constructor.
	 * 
	 * @param elementName String the name of the element
	 * @param selectors   Map&lt;String,String&gt; the list of selectors to use to
	 *                    find the element
	 */
	public WindowsElement(String elementName, Map<String, String> selectors) {
		super(elementName, selectors);
	}

	/**
	 * Returns the Selenium WebElement if it can be found on the current page.
	 * Provides late binding for elements so that the driver does not look for them
	 * until they are called, at which point the driver should be on the correct
	 * page.
	 * 
	 * @return org.openqa.selenium.WebElement the Selenium WebElement object type
	 *         that can be acted upon
	 */
	@Override
	protected WebElement element() {
		WebElement element;
		long searchTime = Time.out().getSeconds() * 1000;
		long startTime = System.currentTimeMillis(); // fetch starting time
		while ((System.currentTimeMillis() - startTime) < searchTime) {
			element = findElementInCurrentFrameForDuration(Time.loopInterval());
			if (element != null)
				return element;
		}
		var errorMessage = SentinelStringUtils.format(
				"{} element named \"{}\" does not exist or is not visible using the following values: {}. Assure you are on the page you think you are on, and that the element identifier you are using is correct.",
				elementType, getName(), selectors);
		throw new NoSuchElementException(errorMessage);
	}

	/**
	 * Takes a Sentinel SelectorType and string value and returns a Selenium By
	 * locator
	 * specifically for Windows elements.
	 * <p>
	 * NOTE: TagName (LocalizedControlType) is unsupported at this time for windows element identification.
	 * This seems to be an appium issue.
	 * </p>
	 * @param selectorType  SelectorType the ENUM value indicating how we search for
	 *                      the element
	 * @param selectorValue String the value being pulled from the config file for
	 *                      the selector
	 * @return org.openqa.selenium.By returns a Selenium By selector for locating an
	 *         element.
	 */
	@Override
	protected By createByLocator(SelectorType selectorType, String selectorValue) {
		switch (selectorType) {
			case ACCESSIBILITYID:
			case AUTOMATIONID:
				return MobileBy.AccessibilityId(selectorValue);
			case CLASS:
			case CLASSNAME:
				return By.className(selectorValue);
			case ID:
			case RUNTIMEID:
				return By.id(selectorValue);
			case NAME:
				return By.name(selectorValue);
			case XPATH:
				return By.xpath(selectorValue);
			default:
				var errorMessage = SentinelStringUtils.format(
						"{} is not a valid selector type for WinAppDriver. Please fix the windows element {} in the {}.yml page object.",
						selectorType, getName(), PageManager.getPage().getName());

				log.error(errorMessage);
				throw new InvalidSelectorException(errorMessage);
		}
	}

	/**
	 * Enter text into a Element. Typically used for text boxes.
	 * This method will throw an ElementDisabledException() if the text box is
	 * disabled.
	 * 
	 * @param text String the text to enter
	 * @return Element (for chaining)
	 */
	@Override
	public Element sendKeys(String text) {
		element().sendKeys(text);
		return this;
	}

	/**
	 * Returns the text of the page element as a String.
	 * 
	 * @return String the text value stored in the element
	 */
	@Override
	public String getText() {
		return element().getText();
	}

	/**
	 * Clear an Element. Clears text in a text box. 
	 * If the call to selenium's clear() fails to actually remove text, 
	 * this method will send CTRL+A + BACKSPACE key chord to attempt to clear the element text.
	 * 
	 * @return Element (for chaining)
	 */
	@Override
	public Element clear() {
		element().clear();
		var remainingText = getText();
		if(StringUtils.isNotEmpty(remainingText)){
			var element = element();
			Actions action = new Actions(Driver.getWebDriver());
			action.keyDown(element, Keys.LEFT_CONTROL)
			.sendKeys("a")
			.keyUp(Keys.LEFT_CONTROL)
			.sendKeys(Keys.BACK_SPACE)
			.perform();

			remainingText = getText();
			if(StringUtils.isNotEmpty(remainingText)){
				var errorMessage = SentinelStringUtils.format(
					"{} on the {} was unable to be cleared.", 
					getName(), PageManager.getPage().getName());
				log.error(errorMessage);
				throw new com.dougnoel.sentinel.exceptions.IOException(errorMessage);
			}
		}
		return this;
	}

	/**
	 * Click an Element.
	 * 
	 * @return Element (for chaining)
	 */
	@Override
	public Element click() {
		element().click();
		return this;
	}

	/**
	 * Returns the Color of the pixel at the given xOffset, yOffset relative to the top left pixel of the element.
	 * WARNING!!! Windows (OS) setting "Make everything bigger" (app and text scaling) must be set to 100% for proper use of this method.
	 * @param xOffset int the pixels to the right of the left edge of the element
	 * @param yOffset int the pixels below the top edge of the element
	 * @return Color the color at the specified pixel offset in the element.
	 */
	public Color getColorAtOffset(int xOffset, int yOffset) {
		// Windows (the operating system) contains a scaling setting which affects the "screen resolution" that Robot reads. 
		// Because Robot respects that setting but the above "BoundingRectangle" coordinates do not, the user must have 100% in that setting so the resolutions are over the same domain.
		BufferedImage screenshot = screenshotElement();

		FileManager.saveImage(null, "tempGetColorImage.png", screenshot);

		int argb = screenshot.getRGB(xOffset, yOffset);
		double a = ((argb >> 24) & 0xFF)/255.0; //The selenium color object will take any double for alpha, but requires 0.0-1.0 for correct operation. This converts the alpha from 0-255 to the correct range.
		int r = (argb >> 16) & 0xFF;
		int g = (argb >> 8) & 0xFF;
		int b = (argb) & 0xFF;

		return new Color(r, g, b, a);
	}

	/**
	 * Returns the Color of the pixel one below and one to the right of the top left pixel of the element.
	 * WARNING!!! Windows (OS) setting "Make everything bigger" (app and text scaling) must be set to 100% for proper use of this method.
	 * @return Color the color of the pixel one below and one to the right of the top left pixel of the element.
	 */
	public Color getColorAtOffset() {
		return getColorAtOffset(1, 1);
	}

	/**
	 * Returns true if the element has an attribute equal to the value passed;
	 * otherwise returns false.
	 * For Windows elements, if attribute == "color" the color is checked via screenshot analysis. Otherwise attribute is checked by grabbing attribute off the element.
	 * <p>
	 * <b>Examples:</b>
	 * <ul>
	 * <li>Determine if an element has a style="display:none" attribute set.
	 * </ul>
	 * 
	 * @param attribute String the attribute to look for
	 * @param value String the value to which attribute should be checked against
	 * @return boolean true if the element as an attribute equal to the value passed; otherwise returns false
	 */
	@Override
	public boolean attributeEquals(String attribute, String value) {
		if (attribute.equalsIgnoreCase("color")) {
			var hexColor = getColorAtOffset().asHex();
			return hexColor.equalsIgnoreCase(value);
		}
		return super.attributeEquals(attribute, value);
	}

	/**
	 * Windows does not support color information, as such this is an unsupported operation for windows elements.
	 *
	 * @return Color Java color object of the background color of the element
	 */
	@Override
	public java.awt.Color getBackgroundColor() {
		throw new NotImplementedException("Windows elements do not support background color");
	}

	/**
	 * Returns a screenshot File of the current element.
	 *
	 * @return File a screenshot of the current element
	 */
	@Override
	public File getScreenshot() {
		BufferedImage temporaryBuffer = screenshotElement();
		return FileManager.saveImage(null, "tempScreenshotImage.png", temporaryBuffer);
	}

	/**
	 * Returns a BufferedImage taken of the current element
	 *
	 * @return BufferedImage A robot screenshot cropped to the current element
	 */
	private BufferedImage screenshotElement() {
		Robot robot;
		try {
			robot = new Robot();
		} catch (AWTException awte) {
			String errorMessage = SentinelStringUtils.format("Failed to screenshot the element {} on the page {}", getName(), PageManager.getPage().getName());
			throw new com.dougnoel.sentinel.exceptions.IOException(errorMessage, awte);
		}

		// Grab the pixel coordinates of the rectangle of the element, relative to the top-left corner of the screen. Positive X = right. Positive Y = down.
		// These coordinates are given in the "true" pixel count of the screen. That is, on a 3840 x 2160 pixel (4K) screen, the coordinates given here are in that domain.
		var elementRectangle = element().getAttribute("BoundingRectangle").split(" ");
		var topLeftX = Integer.parseInt(elementRectangle[0].split(":")[1]);
		var topLeftY = Integer.parseInt(elementRectangle[1].split(":")[1]);
		var width = Integer.parseInt(elementRectangle[2].split(":")[1]);
		var height = Integer.parseInt(elementRectangle[3].split(":")[1]);

		var elementCoords = new java.awt.Rectangle(topLeftX, topLeftY, width, height);
		// Windows (the operating system) contains a scaling setting which affects the "screen resolution" that Robot reads.
		// Because Robot respects that setting but the above "BoundingRectangle" coordinates do not, the user must have 100% in that setting so the resolutions are over the same domain.
		return robot.createScreenCapture(elementCoords);
	}
}