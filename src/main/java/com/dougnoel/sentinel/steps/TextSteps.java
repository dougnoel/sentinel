package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;

import com.dougnoel.sentinel.elements.Element;
import com.dougnoel.sentinel.webdrivers.Driver;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.dougnoel.sentinel.configurations.Configuration;

import io.cucumber.java.en.When;

public class TextSteps {

	/**
     * Appends or prepends random alphanumeric text or unique system time in milliseconds
     * to the given text and enters this new text into a text box that matches the given elementName
     * as defined on the Page object. The given element name is made lower-case and whitespaces are replaced with underscores,
     * then it is sent a sendKeys event to an element defined on a page object with that name. The
     * page object and driver object are defined by the WebDriverFactory and PageFactory objects.
     * The derived Page Object (extends Page) should define a method named [element name]_[element type]
     * returning a Element object (e.g. password_field).
     * <br><br>
     * Since this random value might need to be referenced again, we store it using
     * the ConfigurationManager, using the passed elementname as the key to retrieve
     * it.
     * <br><br>
     * <b>Note:</b> You can optionally specify <b>uniquely</b> rather than <b>randomly</b> to append/prepend
     * the current system time in milliseconds to the given text.
     * <br><br>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I <u>randomly</u> <u>enter</u> <u>random User Name Bob</u> in the <u>username textbox</u></li>
     * <li>I <u>randomly</u> <u>append</u> to <u>random User Name Bob</u> in the <u>username textbox</u></li>
     * <li>I <u>randomly</u> <u>prepend</u> to <u>@gmail.com</u> in the <u>Email Field</u></li>
     * <li>I <u>uniquely</u> <u>prepend</u> <u>@gmail.com</u> in the <u>Email Field</u></li>
     * </ul>
     *
     * @param operation String whether to enter/append or prepend randomly to the given text
     * @param text String the text to enter into the element
     * @param elementName String the name of the element into which to enter text, and the name to store the text under
     */
    @When("^I (randomly|uniquely) (enter|append|prepend)(?: to)? (.*) in the (.*)$")
    public static void enterRandomText(String alphanumericOrNumeric, String operation, String text, String elementName) {
        String randomCharacters;

        if(alphanumericOrNumeric.equals("randomly"))
            randomCharacters = RandomStringUtils.randomAlphanumeric(16);
        else
            randomCharacters = Long.toString(System.currentTimeMillis());

        if(operation.contains("prepend"))
            text = randomCharacters + text;
        else
            text = text + randomCharacters;

        enterText(text, elementName);
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
        getElement(elementName).sendKeys(text);
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
     * Sends a key press event for a special key: BACKSPACE, ESCAPE, ENTER, RETURN, or TAB.
     * @param keyName
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I press the escape key</li>
     * <li>I press the enter key</li>
     * <li>I press the tab key</li>
     * </ul>
     */
    @When("^I press the (backspace|escape|enter|return|tab) key$")
    public static void keyPress(String keyName) {
        if(keyName.equals("backspace"))
            keyName = "BACK_SPACE";
        else
            keyName = keyName.toUpperCase();
    	var driver = Driver.getWebDriver();
        WebElement element = driver.findElement(By.tagName("Body"));
        element.sendKeys(Keys.valueOf(keyName));
    }

    /**
     * Sends a key press event for a special key: BACKSPACE, ESCAPE, ENTER, RETURN, or TAB to a given element.
     * @param keyName
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I press the escape key on the search box</li>
     * <li>I press the enter key in the search box</li>
     * </ul>
     */
    @When("^I press the (backspace|escape|enter|return|tab) key (?:to|on|in) the (.*)$")
    public static void keyPressElement(String keyName, String element) {
        if(keyName.equals("backspace"))
            keyName = "BACK_SPACE";
        else
            keyName = keyName.toUpperCase();
        Element targetElement = getElement(element);
        targetElement.sendSpecialKey(Keys.valueOf(keyName));
    }
    
    /**
     * Clears the text in a text box that matches the given elementName as defined on the 
     * current Page object, and clears any value stored in the
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
     * <li>I clear the username textbox</li>
     * <li>I clear the Password field</li>
     * <li>I clear the email field</li>
     * </ul>
     * 
     * @param elementName String the name of the element to clear
     */
    @When("^I clear the (.*)$")
    public static void clearText(String elementName) {
        getElement(elementName).clear();
        Configuration.clear(elementName);
    }
    
}