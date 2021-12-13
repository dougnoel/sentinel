package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElementAsTextbox;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

import io.cucumber.java.en.When;

public class TextSteps {

	/**
     * Appends random text to the given text and enters this new text into a text box that matches the given elementName
     * as defined on the Page object. The given element name is made lower  case and whitespaces are replaced with underscores,
     * then it is sent a sendKeys event to an element defined on a page object with that name. The
     * page object and driver object are defined by the WebDriverFactory and PageFactory objects.
     * The derived Page Object (extends Page) should define a method named [element name]_[element type]
     * returning a Element object (e.g. password_field).
     * <p>
     * Since this random value might need to be referenced again, we store it using
     * the ConfigurationManager, using the passed elementname as the key to retrieve
     * it.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I enter random User Name Bob in the username textbox</li>
     * <li>I enter random text in the Street Address field</li>
     * </ul>
     * 
     * @param text String the text to enter into the element
     * @param elementName String the name of the element into which to enter text
     * @param storageName String an additional modifier for storing data
     */
    @When("^I randomly enter (.*) in the (.*)(?: for the (.*))$")
    public static void enterRandomText(String text, String elementName, String storageName) {
        text = text + RandomStringUtils.randomAlphanumeric(16);
        enterText(text, elementName);
        if (storageName != null) {
            elementName = storageName + " " + elementName;
        }
        Configuration.update(elementName, text);
    }

    /**
     * Enters the text into a text box that matches the given elementName as defined on the 
     * current Page object, and stores the text entered into the
     * Configuration Manager using the element name as the key. The given elementName is
     * made lower case and whitespaces are replaced with underscores, then it is
     * sent a sendKeys event to an element defined on a page object with that name.
     * The page object and driver object are defined by the WebDriverFactory and
     * PageFactory objects. The derived Page Object (extends Page) should define a
     * method named [element name]_[element type] returning a Element object
     * (e.g. password_field).
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I enter bob in the username textbox</li>
     * <li>I enter abc123 in the Password field</li>
     * <li>I went test@test.com in the email field</li>
     * </ul>
     * 
     * @param text String the text to enter into the element
     * @param elementName String the name of the element into which to enter text
     */
    @When("^I enter (.*) in the (.*)$")
    public static void enterText(String text, String elementName) {
        getElementAsTextbox(elementName).type(text);
        Configuration.update(elementName, text);
    }
    
    /**
     * Types the randomly generated text for the given key under which the text is stored in the given elementName.
	 * The given values can have the same name as it looks up a stored value for that element. This should be called only after using the
     * method i_enter_random_text_in_the_textbox_named(). It then types that value into the element using sendKeys(). 
     * This step will work across steps and tests by using dependency injection (DI).
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     *  <li>I reuse the email text in the email field</li>
     *  <li>I reuse the password text in the password field</li>
     *  <li>I reuse the username text in the username field.
     * </ul>
     * @param key String the key used to retrieve the value
     * @param elementName String the name of the element into which to enter text
     */
    @When("^I reuse the (.*) text in the (.*)$")
    public static void enterStoredText(String key, String elementName) {
    	var text = Configuration.toString(key);
        enterText(text, elementName);
    }

    /**
     * Sends a key press event for a special key: ESCAPE, ENTER, RETURN, or TAB.
     * @param keyName
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I press the escape key</li>
     * <li>I press the enter key</li>
     * <li>I press the tab key</li>
     * </ul>
     */
    @When("^I press the (escape|enter|return|tab) key$")
    public static void keyPress(String keyName) {
    	keyName = keyName.toUpperCase();
    	var driver = WebDriverFactory.getWebDriver();
        WebElement element = driver.findElement(By.tagName("Body"));
        element.sendKeys(Keys.valueOf(keyName));
    }
    
}
