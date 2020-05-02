package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElementAsTextbox;

import org.apache.commons.lang3.RandomStringUtils;

import com.dougnoel.sentinel.configurations.ConfigurationManager;

import cucumber.api.java.en.When;

public class TextSteps {

    /**
     * Appends random text to the given text and enters this new text into a text box that matches the given elementName
     * as defined on the Page object. The given element name is made lower  case and whitespaces are replaced with underscores,
     * then it is sent a sendKeys event to an element defined on a page object with that name. The
     * page object and driver object are defined by the WebDriverFactory and PageFactory objects.
     * The derived Page Object (extends Page) should define a method named [element name]_[element type]
     * returning a PageElement object (e.g. password_field).
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
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I randomly enter (.*) in the (.*)(?: for the (.*))$")
    public static void i_enter_random_text_in_the_textbox_named(String text, String elementName, String storageName)
            throws Throwable {
        text = text + RandomStringUtils.randomAlphanumeric(16);
        i_enter_text_in_the_textbox_named(text, elementName);
        if (storageName != null) {
            elementName = storageName + " " + elementName;
        }
        ConfigurationManager.setValue(elementName, text);
    }
    
    /**
     * Overloaded method for entering random text in a text box, so that nulls do not need to be passed. 
     * Intended for use in creating complex Cucumber steps definitions.
     * 
     * @param text String the text to enter into the element
     * @param elementName String the name of the element into which to enter text
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    public static void i_enter_random_text_in_the_textbox_named(String text, String elementName) throws Throwable {
    	i_enter_random_text_in_the_textbox_named(text, elementName, null);
    }

    /**
     * Enters the text into a text box that matches the given elementName as defined on the 
     * current Page object, and stores the text entered into the
     * Configuration Manager using the element name as the key. The given elementName is
     * made lower case and whitespaces are replaced with underscores, then it is
     * sent a sendKeys event to an element defined on a page object with that name.
     * The page object and driver object are defined by the WebDriverFactory and
     * PageFactory objects. The derived Page Object (extends Page) should define a
     * method named [element name]_[element type] returning a PageElement object
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
     * @throws Throwable this exists so that any uncaught exceptions result in the testfailing
     */
    @When("^I enter (.*) in the (.*)$")
    public static void i_enter_text_in_the_textbox_named(String text, String elementName) throws Throwable {
        getElementAsTextbox(elementName).type(text);
        ConfigurationManager.setValue(elementName, text);
    }

    /**
     * Enters the given text into a text box that matches the given elementName as defined on the current Page object,
     * then stores the text value in the Configuration Manager using the given element name as the key.
     * The given element name is made lower case and whitespaces are replaced with
     * underscores in getElementAsTextbox, then it is sent a pressKeys event to an element defined on a
     * page object with that name. The page object and driver object are defined by
     * the WebDriverFactory and PageFactory objects. The derived Page Object
     * (extends Page) should define a method named [element name]_[element type]
     * returning a PageElement object (e.g. password_field).
     * <p>
     * This is different from just entering text in that the text is injected into
     * the element by executing javascript. This will deal with javascript sending
     * key events to a hidden field for text processing when the sendKeys() method
     * isn't working.
     * <p>
     * NOTE: This should only be used if the standard "I enter &lt;text&gt; in the
     * &lt;element&gt; step does not work. It is not representative of typical user
     * action.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I inject bob in the username textbox</li>
     * <li>I inject abc123 in the Password field</li>
     * <li>I inject test@test.com into the email field<li>
     * </ul>
     * 
     * @param text String the text to enter
     * @param elementName String the name of the element into which to enter text
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I inject (.*) in the (.*)$")
    public static void i_inject_text_in_the_textbox_named(String text, String elementName) throws Throwable {
        getElementAsTextbox(elementName).javaScriptSendKeys(text);
        ConfigurationManager.setValue(elementName, text);
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
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I reuse the (.*) text in the (.*)$")
    public static void i_use_the_randomly_generated_text_for_the_textbox_named(String key, String elementName)
            throws Throwable {
        String text = ConfigurationManager.getValue(key);
        i_enter_text_in_the_textbox_named(text, elementName);
    }
    
    /**
     * Enters username and password information into specified fields based on the environment, and optionally
     * a specific account or account type for the environment.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I fill the account information into the username field and password field</li>
     * <li>I fill the account information for account Tester into the username box and the password box</li>
     * <li>I fill the account information for account SystemEnv into the username field and the password field</li>
     * </ul>
     * @see com.dougnoel.sentinel.configurations.ConfigurationManager#getUsername()
     * @see com.dougnoel.sentinel.configurations.ConfigurationManager#getPassword()
     * @see com.dougnoel.sentinel.configurations.ConfigurationManager#getUsername(String)
     * @see com.dougnoel.sentinel.configurations.ConfigurationManager#getPassword(String)
     * @param usingAccountName String if we have a user account, get the info from its account
     * @param account String user account
     * @param username_field String username field
     * @param password_field String password field
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I fill the account information( for account (.*?))? into the (.*?) and the (.*?)$")
    public static void i_fill_the_account_information_into_the_username_field_and_the_password_field(String usingAccountName, String account, String username_field, String password_field) throws Throwable {
        if (usingAccountName == null) {
            i_enter_text_in_the_textbox_named(ConfigurationManager.getUsername(), username_field);
            i_enter_text_in_the_textbox_named(ConfigurationManager.getPassword(), password_field);            
        } else {
            i_enter_text_in_the_textbox_named(ConfigurationManager.getUsername(account), username_field);
            i_enter_text_in_the_textbox_named(ConfigurationManager.getPassword(account), password_field); 
        }           
    }
    
    /**
     * Enters test data into specified fields based on the environment
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I fill the test data for Test User of the MemberId into the eligibility search memberid field</li>
     * </ul>
     * @see com.dougnoel.sentinel.configurations.ConfigurationManager#getTestData(String, String)
     * @param testData String is test data from configuration
     * @param key String is a key of the test data from configuration
     * @param elementName String any entry field
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I fill the test data for (.*?) of the (.*?) into the (.*?)$")
    public static void i_fill_the_test_data_into_the_key_field(String testData, String key, String elementName) throws Throwable {
    	
    	i_enter_text_in_the_textbox_named(ConfigurationManager.getTestData(testData, key), elementName);
    	
    }

    /**
     * Enters password information into the specified field based on the environment, and optionally
     * a specific account or account type for the environment.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I fill the password into the password field</li>
     * <li>I fill the password for account Tester into the password box</li>
     * <li>I fill the password for account SystemEnv into the password field</li>
     * </ul>
     * @see com.dougnoel.sentinel.configurations.ConfigurationManager#getPassword()
     * @see com.dougnoel.sentinel.configurations.ConfigurationManager#getPassword(String)
     * @param usingAccountName String if we have a user account, get the info from its account
     * @param account String user account
     * @param password_field String password field
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I fill the password( for account (.*?))? into the (.*?)$")
    public static void i_fill_the_password_into_the_password_field(String usingAccountName, String account, String password_field) throws Throwable {
        if (usingAccountName == null) {
            i_enter_text_in_the_textbox_named(ConfigurationManager.getPassword(), password_field);            
        } else {
            i_enter_text_in_the_textbox_named(ConfigurationManager.getPassword(account), password_field); 
        }           
    }
    
    /**
     * Enters username  information into specified fields based on the environment, and optionally
     * a specific account or account type for the environment.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I fill the username into the username field</li>
     * <li>I fill the username for account Tester into the username box</li>
     * <li>I fill the username for account SystemEnv into the User Name field</li>
     * </ul>
     * @see com.dougnoel.sentinel.configurations.ConfigurationManager#getUsername()
     * @see com.dougnoel.sentinel.configurations.ConfigurationManager#getUsername(String)
     * @param usingAccountName String if we have a user account, get the info from its account
     * @param account String user account
     * @param username_field String username field
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I fill the username( for account (.*?))? into the (.*?)$")
    public static void i_fill_the_username_into_the_username_field(String usingAccountName, String account, String username_field) throws Throwable {
        if (usingAccountName == null) {
            i_enter_text_in_the_textbox_named(ConfigurationManager.getUsername(), username_field);
        } else {
            i_enter_text_in_the_textbox_named(ConfigurationManager.getUsername(account), username_field);
        }           
    }

    /**
     * Sends key press and release events to the given field.
     * 
     * NOTE: This does not currently appear to be working. This method takes two
     * strings. The first string is text to type into an element. The second is made
     * lower case and whitespaces are replaced with underscores, then it is sent a
     * pressKeys event to an element defined on a page object with that name. The
     * page object and driver object are defined by the WebDriverFactory and
     * PageFactory objects. The derived Page Object (extends Page) should define a
     * method named [element name]_[element type] returning a PageElement object
     * (e.g. password_field).
     * <p>
     * This is different from just entering text in that every key press and key
     * release is actually sent to the field. This will deal with javascript sending
     * key events to a hidden field for text processing when the sendKeys() method
     * isn't working.
     * <p>
     * This method will also work if you want to send focus to a div, image, etc and
     * press a key on that element.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I press the keys bob in the username textbox</li>
     * <li>I press the keys abc123 in the Password field</li>
     * <li>I press the keys 123 Main Street in the address field</li>
     * </ul>
     * 
     * @param text String text the text to enter
     * @param elementName String the name of the element into which to enter text
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I press the keys (.*) in the (.*)$")
    public static void i_press_the_keys_in_the_element(String text, String elementName) throws Throwable {
        getElementAsTextbox(elementName).pressKeys(text);
        ConfigurationManager.setValue(elementName, text);
    }
    
}
