package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.webdrivers.Driver;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

/**
 * Methods used to define basic operations.
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
     * Page) should define a method named [element name]_[element type] returning a Element object (e.g. login_button).
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I click a login button</li>
     * <li>I click the Login button</li>
     * <li>I click an Operation Button</li>
     * </ul>
     * 
     * @param elementName String the name of the element to click
     */
    @When("^I click (?:the|a|an|on) (.*?)$")
    public static void click(String elementName) {
        getElement(elementName).click();
    }

    /**
     * Hovers the middle of the given element
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I hover a login button</li>
     * <li>I hover over the Login button</li>
     * <li>I hover an Operation Button</li>
     * </ul>
     *
     * @param elementName String the name of the element to hover
     */
    @When("^I hover (?:on |over )?(?:the|a|an)[ ](.*?)$")
    public static void hover(String elementName) {
        getElement(elementName).hover();
    }

    /**
     * Drags the first element onto the second element.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I drag Box A onto Box B</li>
     * <li>I drag the first name to the answer box</li>
     * <li>I drag an animal icon into a habitat icon</li>
     * </ul>
     * @param source String the name of the source element to drag
     * @param target String the name of the target element the source is being dragged to
     * @throws IOException if the javascript drag and drop file cannot be loaded
     */
    
    @When("I drag (?:the |an? )?(.*?) (?:on|in)?to (?:the |an? )?(.*?)$")
    public static void dragAndDropToObject(String source, String target) throws IOException {
    	getElement(source).dragAndDrop(getElement(target));	
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
     * @param seconds double the number of seconds to wait
     */
    @When("^I wait (\\d{1,2}(?:[.,]\\d{1,4})?) seconds?(?:.*)$")
    public static void wait(double seconds) {
    	double totalWaitTime = Configuration.toDouble("totalWaitTime");
    	Configuration.update("totalWaitTime", seconds + totalWaitTime);
        Time.wait(seconds);
        log.warn("Passed {} seconds, waiting {} milliseconds. Waits should only be used for special circumstances. If you are seeing this message a lot then you should probably be logging a bug ticket to get the framework fixed at: http://https://github.com/dougnoel/sentinel/issues", seconds, (seconds * 1000));
    }

    /**
     * Loads a page or starts an executable based on the environment you are currently testing. 
     * The url/executable is set in the page object yaml file.
     * Refer to the documentation in the sentinel.example project for more information. 
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I navigate to the Login Page</li>
     * <li>I start the Notepad Application</li>
     * <li>I open the Calculator Executable</li>
     * <li>I am on the Login Page</li>
     * </ul>
     * <p>
     * SEE ALSO: VerificationSteps.redirectedToPage() for how to switch page objects without starting a new driver.
     * @param pageName String Page Object Name
     */
    @Given("^I (?:navigate to|start|open|am on) the (.*?)$")
    public static void navigateToPage(String pageName) {
    	navigateToPageWithArguments("", pageName);
    }
    
    /**
     * Loads a page based on the environment you are currently testing. The url is set in the page object yaml file.
     * Refer to the documentation in the sentinel.example project for more information. You cannot load a URL
     * directly, because once there you would not be able to do anything.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I pass the argument "?docYear=2003" to the Documents Page</li>
     * <li>I pass the arguments "?firstname=bob{@literal &}lastname=smith" to the Users Page</li>
     * <li>I pass the arguments "/apples/oranges" to the Fruits Page</li>
     * </ul>
     * @param arguments String the literal string to append to the default URL.
     * @param pageName String Page Object Name
     */
    @Given("^I pass the arguments? \"([^\"]*)\" to the (.*?)$")
    public static void navigateToPageWithArguments(String arguments, String pageName) {
    	PageManager.open(pageName, arguments);
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
     * @param option String the browser action
     */
    @When("^I press the browser (back|forward|refresh) button$")
    public static void pressBrowserButton(String option) {
        switch (option) {
        case "back":
            Driver.navigateBack();
            break;
        case "forward":
        	Driver.navigateForward();
            break;
        default:
        	Driver.refresh();
            break;
        }
    }    
    
    /**
     * Interacts with the open JS alert. Accept or close.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I accept the JS alert</li>
     * <li>I close the JS alert</li>
     * </ul>
     * @param action String the action to take on the JS alert
     */
    @When("^I (accept|close) the JS alert$")
    public static void acceptOrCloseJsAlert(String action) {
        if(action.contentEquals("accept"))
            Driver.getWebDriver().switchTo().alert().accept();
        else
        	Driver.getWebDriver().switchTo().alert().dismiss();
    }  
    
}