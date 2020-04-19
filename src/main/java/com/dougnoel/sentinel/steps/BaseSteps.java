package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.*;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.elements.Link;
import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.StringUtils;

import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;

/**
 * Methods used to define basic operations. Other step files can extend or
 * include this one to leverage these actions.
 * 
 * Functionality in this class includes clicking given elements, entering text, selecting items, verifying elements exist, 
 * contains given text, or are active, enabled, or hidden, verifying table columns or table rows have given text, 
 * unique text, text for stored values, navigating results, 
 * and basic browser interaction.
 */
public class BaseSteps {
    private static final Logger log = LogManager.getLogger(BaseSteps.class.getName()); // Create a logger.

    @Before
    public static void before(Scenario scenario) {
        log.trace("Scenario ID: {} Scenario Name: {}", scenario.getId(), scenario.getName());
    }
    
    /**
     * Clicks the element that matches the given elementName as defined on the current Page object. The page object and driver object are defined by the
     * WebDriverFactory and PageFactory objects. The derived Page Object (extends
     * Page) should define a method named [element name]_[element type] returning a PageElement object (e.g. login_button).
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I click a login button</li>
     * <li>I click the Login button</li>
     * <li>I click an Operation Button</li>
     * </ul>
     * 
     * @param elementName String the name of the element to click
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I click (?:the|a|an) (.*?)$")
    public static void i_click_the(String elementName) throws Throwable {
        getElement(elementName).click();
    }

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
     * Selects the given option in the select element, then stores the selected
     * value in the Configuration Manager using the element name as the key. Fails
     * if the option doesn't exist or the element is not visible on the page. This
     * method takes two strings. The first string is text of the option to select.
     * The second is made lower case and whitespaces are replaced with underscores,
     * then it is sent a select event to an element defined on a page object with
     * that name. The page object and driver object are defined by the
     * WebDriverFactory and PageFactory objects. The derived Page Object (extends
     * Page) should define a method named [element name]_[element type] returning a
     * PageSelectElement object (e.g. password_field).
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I select bob in the name dropdown</li>
     * <li>I select Brady from the Last Name list</li>
     * <li>I select Doctor from the providers list</li>
     * </ul>
     * <b>NOTE:</b> The word "the" cannot be used as the beginning of an option to
     * select. This is to disambiguate the method from the method allowing you to
     * select an ordinal option. If you wish to use the word "the" as part of an
     * option, you will need to write a custom step.
     * <p>
     * 
     * @param text String the text of the option to select
     * @param elementName String the name of the select element
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I select (?!the)(.*?) (?:in|from) the (.*)$")
    public static void i_select_item_from_the_element(String text, String elementName) throws Throwable {
        getElementAsSelectElement(elementName).select(text);
        ConfigurationManager.setValue(elementName, text);
    }


	/**
     * Selects the ordinal option on the element and then stores the text value of
     * that selection in the Configuration Manager using the element name as the
     * key.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I select the 1st option in the name dropdown</li>
     * <li>I select 2nd option from the Last Name list</li>
     * <li>I select the last option in the Providers list</li>
     * </ul>
     * <b>NOTE:</b> Numbering start with 1, not 0 to make the cucumber step more
     * human-readable to business owners who do not need to understand numbering in
     * programming usually starts at 0.
     * <p>
     * 
     * @param ordinal String the ordinal selection (numbering starts with 1)
     * @param elementName String the name of the select element
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I select the (\\d+)(?:st|nd|rd|th) option (?:in|from) the (.*)$")
    public static void i_select_ordinal_item_from_an_element(String ordinal, String elementName) throws Throwable {
        int index = Integer.parseInt(ordinal);
        String text = getElementAsSelectElement(elementName).select(index).getText(index);
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
     * Waits for the sum of the given number of seconds and fractions of sections.
     * This step is used to add in an implicit wait time using a Cucumber step.
     * Ideally this should be used to determine if you just need to wait for
     * something to load. Once that is determined, you should be fixing the
     * following method to improve its own wait functionality. In the cases where
     * you have to impose a wait, you should use the smallest amount of time
     * possible. This method can wait as small amount a time as 1 millisecond using
     * decimals. This method deals with whole numbers as well as fractions of a
     * second using decimals. Furthermore, you can put a reason at the end of the
     * step for clarity and the method will ignore that extra text when matching.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I wait 1 second</li>
     * <li>I wait 3 seconds</li>
     * <li>I wait 0.2 seconds</li>
     * <li>I wait 0.5 seconds before continuing</li>
     * <li>I wait 0.001 second to ensure the element is visible before checking
     * it</li>
     * </ul>
     * 
     * @param seconds int Number of whole seconds to wait.
     * @param fraction int Fraction of a second to wait, added to the number above.
     * @throws Throwable Throws any errors passed to it.
     */
    @When("^I wait (\\d+)(?:\\.)?(\\d+) seconds?(?:.*)$")
    public static void i_wait_x_seconds(int seconds, int fraction) throws Throwable {
        long milliseconds = (long) seconds * 1000;
        if (fraction != 0) {
            float secs = (float) seconds + (float) fraction;
            milliseconds = (long) (secs * 1000);
        }
        Thread.sleep(milliseconds);
    }

    /**
     * Loads a page based on the environment you are currently testing. The url is set in the page object yaml file.
     * Refer to the documentation in the sentinel.example project for more information. You cannot load a URL
     * directly, because once there you would not be able to do anything.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I navigate to the Login Page</li>
     * <li>I remain on the Documents page using the argument ?docYear=2003</li>
     * <li>I am on the home page using the arguments ?firstname=bob&lastname=smith</li>
     * </ul>
     * @param pageName String Page Object Name
     * @param hasArguments boolean indicates whether there is a query string to add to the usual URL
     * @param arguments String the literal string to append to the default URL.
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Given("^I (?:navigate to|am on|remain on) the (.*?) (?:P|p)age( using the arguments? )?(.*?)?$")
    public static void i_am_on_the_page(String pageName, String hasArguments, String arguments) throws Throwable {   	
    	pageName = pageName.replaceAll("\\s", "") + "Page";
        PageManager.setPage(pageName);
        String baseUrl = ConfigurationManager.getUrl();
        if (hasArguments != null) {
            baseUrl += arguments;
        }
        log.debug("Loading {} for the {} in the {} environment.", baseUrl, pageName, ConfigurationManager.getEnvironment());
        PageManager.openPage(baseUrl);
    }
    
    /**
     * Overloaded method for navigating to a page object's url based on the current environment, 
     * so that nulls do not need to be passed. Intended for use in creating complex Cucumber steps definitions.
     * @param pageName String Page Object Name
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    public static void i_am_on_the_page(String pageName) throws Throwable {
    	i_am_on_the_page(pageName, null, null);
    }
    
    /**
     * Overloaded method for navigating to a page object's url based on the current environment, and taking a 
     * query string argument it will append to the request. Used so that nulls do not need to be passed. Intended 
     * for use in creating complex Cucumber steps definitions.
     * @param pageName String Page Object Name
     * @param arguments String the literal string to append to the default URL.
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    public static void i_am_on_the_page(String pageName, String arguments) throws Throwable {
    	i_am_on_the_page(pageName, "has arguments", arguments);
    }
 
    /**
     * Closes the child browser tab or window
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I close the browser tab</li>
     * <li>I close the browser window"</li>
     * </ul>
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I close the browser (?:tab|window)$")
    public static void i_close_the_browser_tab() throws Throwable {
        PageManager.closeChildWindow();
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
     * Clicks the link in a table row by matching text in another part of the row.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I find the row matching the text stored for the &lt;Client&gt; Login ID
     * field in the search results table and click the Edit link</li>
     * <li>I find the row matching the text stored for the current user in the
     * Search Results Table and click the Edit button</li>
     * <li>I find the link in the row of the Provider Search
     *  table containing the search criteria values and click the search button.</li>
     * </ul>
     * 
     * @param key String the key used to retrieve the value
     * @param tableName String the name of the table to search
     * @param elementName String the name of the element to click
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I find the (.*?) link in the row of the (.*?) containing the (.*?) value and click it$")
    public static void i_find_the_row_matching_the_text_stored_for_the_element_in_the_table_and_click_an_associated_link(
            String elementName, String tableName, String key) throws Throwable {
        String text = ConfigurationManager.getValue(key);
        getElementAsTable(tableName).clickElementInRowThatContains(text, elementName);
    }
    
    /**
     * Clicks a text value or xpath part in a table that contains another text value or xpath part.
     * Can be used to click a chevron or image in a table that is related to some piece of data
     * in the table. For example: an expand, edit or delete button.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I find the Users Table and click the text Edit in the row containing the text Sally Smith</li>
     * <li>I find the Cars Table and click the xpath //span[@class="cke_button_icon"] in the row containing the text Toyota Avalon</li>
     * <li>I find the Categories Table and click the xpath //img[1] in the row containing the xpath //img[starts-with(@id,'uid')]</li>
     * <li>I find the Example Table and click the text Delete in the row containing the xpath //*[name()="svg" and @class="svg-connector"]/*[name()="circle" and @class="inner-circle"]</li>
     * </ul>
     * @param tableName String the name of the table to search
     * @param clickLocatorType String the type of locator the next value will be: text or xpath
     * @param elementToClick String the text or xpath used to find the item you want to click upon
     * @param matchLocatorType String the type of locator the next value will be: text or xpath
     * @param elementToMatch String the text or xpath of the value that will ensure you are in the correct row
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I find the (.*?) and click the (text|xpath) (.*?) in the row containing the (text|xpath) (.*?)$")
    public static void i_find_the_table_and_click_the_link_associated_with_the_locator(
    		String tableName, String clickLocatorType, String elementToClick, String matchLocatorType, String elementToMatch) throws Throwable {
    	By clickLocator;
    	if ( StringUtils.equals(clickLocatorType, "xpath") ) {
    		clickLocator = By.xpath(elementToClick);
    	} else {
    		clickLocator = By.xpath("//*[contains(text(),'" + elementToClick + "')]");
    	}
    	By matchLocator;
    	if ( StringUtils.equals(matchLocatorType, "xpath") ) {
    		matchLocator = By.xpath(elementToMatch);
    	} else {
    		matchLocator = By.xpath("//*[contains(text(),'" + elementToMatch + "')]");
    	}
    	
    	getElementAsTable(tableName).clickElementInRowThatContains(matchLocator, clickLocator);
    }
    
    /**
     * Clicks a text value, stored value or xpath part in a table in a row. The row is determined by
     * ordinal value (last, 1st, 2nd, 3rd, etc.).
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I find the 1st row in the Soil Table and click the text Dirt</li>
     * <li>I find the 2nd row in the Garbage Table and click the xpath //*[@id = 'myValue']</li>
     * <li>I find the last row in the Users Table and click the value for Username</li>
     * </ul>
     * @param ordinal String the row number to search; pass "la" or -1 to get the last row
     * @param tableName String the name of the table to search
     * @param clickLocatorType String the type of locator the next value will be: text, xpath, value for (stored value)
     * @param elementToClick  String the text, xpath or stored value used to find the item you want to click upon
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @When("^I find the (\\d+|la)(?:st|nd|rd|th) row in the (.*?) and click the (text|xpath|value for) (.*?)$")
    public static void i_find_the_ordinal_row_in_the_table_and_click_an_associated_link(
    		String ordinal, String tableName, String clickLocatorType, String elementToClick) throws Throwable {
    	By clickLocator;
    	int ordinalRow;
    	switch (clickLocatorType) {
    	case "xpath":
    		clickLocator = By.xpath(elementToClick);
    		break;
    	case "value for":
    		elementToClick = ConfigurationManager.getValue(elementToClick);
    		// If we are retrieving a value we want to fall through here
    	default:
    		clickLocator = By.xpath("//*[contains(text(),'" + elementToClick + "')]");
    		break;
    	}
    	if ( StringUtils.equals(ordinal, "la") ) { 
    		ordinalRow = -1;
    	} else {
    		ordinalRow = Integer.parseInt(ordinal);
    	}
    	
    	getElementAsTable(tableName).clickElementInRowThatContains(ordinalRow, clickLocator);
    }

    /**
     * Takes a link name and an extension type. Expects the document to be opened in a new tab/window.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I open the .docx file in a new tab</li>
     * <li>I open the pdf in a new tab"</li>
     * <li>I open the .md file in a new tab</li>
     * </ul>
     * @param linkName String The text of the Download link. NOT a PageElement object name.
     * @param extension String Extension type (.txt/.pdf/.etc) of the file being downloaded.
     * @throws Throwable Passes through any errors to the executing code.
     */
    @When("^I open the (.*?)( pdf)? in a new tab$")
    public static void i_open_the_document_with_extension_in_a_new_tab(String linkName, String extension) throws Throwable {
    	if (extension == null) {
        	extension = "pdf";
        	getElement(linkName).click();
        } else {
        	extension = StringUtils.strip(extension);
           new Link(SelectorType.PARTIALTEXT, linkName).click();
        }
        PageManager.waitForPageLoad();
        PageManager.switchToNewWindow();
        String pdfUrl = PageManager.getCurrentUrl();
        String expectedResult = StringUtils.format("Expected URL \"{}\" to contain the .{} extension.", pdfUrl,
                extension);
        assertTrue(expectedResult, pdfUrl.contains("." + extension));
    }
    
    /**
     * Navigates through basic browser actions: forward, back and refresh
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I press the browser forward button</li>
     * <li>I press the browser refresh button</li>
     * <li>I press the browser back button</li>
     * </ul>
     * @see com.dougnoel.sentinel.pages.PageManager#navigateBack()
     * @see com.dougnoel.sentinel.pages.PageManager#navigateForward()
     * @see com.dougnoel.sentinel.pages.PageManager#refresh()
     * @param option String the browser action
     * @throws Throwable Passes through any errors to the executing code.
     */
    @When("^I press the browser (back|forward|refresh) button$")
    public static void i_press_the_browser(String option) throws Throwable {
        switch (option) {
        case "back":
            PageManager.navigateBack();
            break;
        case "forward":
            PageManager.navigateForward();
            break;
        default:
            PageManager.refresh();
            break;
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

    /**
     * Stores the data for the given page in the given table for later comparison.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I view the 1st page of results from the Provider search </li>
     * <li>I view the 5th page of results from the Members table</li>
     * <li>I view the 2nd page of results from the NFL Schedule </li>
     * </ul>
     * @param pageNumber int Page we are on to store as a key in a hash.
     * @param tableName String the name of the table element on the page object
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Given("^I view the (\\d+)(?:st|nd|rd|th) page of results from the (.*)$")
    public static void i_view_the_x_page_of_results(int pageNumber, String tableName) throws Throwable {
        getElementAsTable(tableName).storeTable(pageNumber);
    }
    
}