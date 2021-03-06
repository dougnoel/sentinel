package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import io.cucumber.java.en.Then;

/**
 * Methods used to defined basic validations
 *
 */
public class VerificationSteps {
    
    /**
     * Verifies the given element exists. The given element string is made lower case 
     * and whitespaces are replaced with underscores, then it is sent the isDisplayed
     * event and returns true or false. It also has an assertion variable which is set
     * if the words "does not" are used, and it looks for a negative assertion. The word 
     * "does" can be used optionally, and was added to support the use of a Scenario Outline which
     * switches between "does" and "does not" in its tests.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify a submit button exists</li>
     * <li>I verify the Plan Dropdown does not exist</li>
     * <li>I verify an area map does exist</li>
     * </ul>
     * <b>Scenario Outline Example:</b>
     * <p>
     * I verify the &lt;element&gt; &lt;Assertion&gt; exists
     * <p>
     * Examples:<br>
     *   | element         | Assertion |<br>
     *   | first dropdown  | does      |<br>
     *   | second dropdown | does not  |
     * 
     * @param elementName String Element to check
     * @param assertion String "does" or does not" for true or false
     */
    @Then("^I verify (?:the|a|an) (.*?)( does not)?(?: does)? exists?$")
    public static void verifyElementExists(String elementName, String assertion) {
        boolean negate = !StringUtils.isEmpty(assertion);
        String expectedResult = SentinelStringUtils.format("Expected the element {} to {}exist.",
                elementName, (negate ? "not " : ""));
        if (negate) {
            assertTrue(expectedResult, getElement(elementName).doesNotExist());
        } else {
            assertTrue(expectedResult, getElement(elementName).isDisplayed());
        }
    }
    
    /**
     * Verifies the whether the given element is selected or not. Intended to be used with check boxes and radio buttons.
     * Using the optional word not will check to make sure the item is not checked/selected.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the Yes check box is checked</li>
     * <li>I verify the Female Radio Button is selected</li>
     * <li>I verify the I would like fries with that Check box is not selected</li>
     * </ul>
     * @param elementName String Element to check
     * @param assertion String "" or " not" for true or false
     */
    @Then("^I verify(?: the)? (.*) is (not |un)?(?:checked|selected)$")
    public static void verifyElementIsSelected(String elementName, String assertion) {
        boolean negate = !StringUtils.isEmpty(assertion);
        String expectedResult = SentinelStringUtils.format("Expected the element {} to {} selected.",
                elementName, (negate ? "not be" : "be"));
        if (negate) {
            assertTrue(expectedResult, getElement(elementName).isNotSelected());
        } else {
            assertTrue(expectedResult, getElement(elementName).isSelected());
        }
    }
    
    /**
     * Verifies an element has an attribute.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the table has the attribute table-striped</li>
     * <li>I verify the textbox does not have the attribute listStyleClass</li>
     * <li>I verify the div has the attribute container</li>
     * </ul>
     * @param elementName String element to inspect
     * @param assertion String "has" for a positive check, anything else for negative
     * @param attribute String class attribute to verify
     */
    @Then("^I verify (?:the|a|an) (.*?) (has|does not have) the attribute (.*?)$")
    public static void verifyElementHasAttribute(String elementName, String assertion, String attribute) {
        String expectedResult = SentinelStringUtils.format("Expected the element {} {} the attribute \"{}\".",
                elementName, assertion, attribute);
        if (assertion.contentEquals("has")) {
            assertTrue(expectedResult, getElement(elementName).hasAttribute(attribute));
        } else {
            assertTrue(expectedResult, getElement(elementName).doesNotHaveAttribute(attribute));
        }
    }
    
    /**
     * Verifies an element has a class.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the table has the class heading"</li>
     * <li>I verify the textbox does not have the class style"</li>
     * <li>I verify the div has the class aria</li>
     * </ul>
     * @param elementName String element to inspect
     * @param assertion String "has" for a positive check, anything else for negative
     * @param className String class to verify
     */
    @Then("^I verify (?:the|a|an) (.*?) (has|does not have) the class (.*?)$")
    public static void verifyElementHasClass(String elementName, String assertion, String className) {
    	verifyElementAttributeHasValue(elementName, assertion, "class", className);
    }
    
    /**
     * Verifies an attribute for an element has a given value.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the boxes table with the attribute class has the value heading"</li>
     * <li>I verify the New Div with the attribute style does not have the value aria"</li>
     * <li>I verify the Submit Button with the attribute color has the value blue</li>
     * </ul>
     * @param elementName String element to inspect
     * @param assertion String "has" for a positive check, anything else for negative
     * @param attribute String attribute to inspect
     * @param value String value expected
     */
    @Then("^I verify (?:the|a|an) (.*?) with (?:the|a|an) attribute (.*?) (has|does not have) (?:the|a|an) value (.*?)$")
    public static void verifyElementAttributeHasValue(String elementName, String assertion, String attribute, String value) {
        String expectedResult = SentinelStringUtils.format("Expected the element {} {} the attribute \"{}\" with the value {}.",
                elementName, assertion, attribute, value);
        if (assertion.contentEquals("has")) {
            assertTrue(expectedResult, getElement(elementName).attributeEquals(attribute, value));
        } else {
            assertFalse(expectedResult, getElement(elementName).attributeEquals(attribute, value));
        }
    }
    
    /**
     * Verifies an element is enabled by asserting the element found for the given element name is enabled
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the submit button is not enabled</li>
     * <li>I verify the home link is enabled</li>
     * <li>I verify the link to open a pdf is enabled</li>
     * </ul>
     * @param elementName String the name of the element
     * @param assertion String "enabled" for a positive check, anything else for negative
     */
    @Then("^I verify (?:the|a|an) (.*?) is (enabled|disabled)$")
    public static void verifyElementIsEnabled(String elementName, String assertion) {
        String expectedResult = SentinelStringUtils.format("Expected the element {} to be {}.",
                elementName, assertion);
        if (assertion.contentEquals("enabled")) {
        	assertTrue(expectedResult, getElement(elementName).isEnabled());
        } else {
        	assertTrue(expectedResult, getElement(elementName).isDisabled());
        }
    }
    
    /**
     * Verifies an element is enabled by asserting the element for the given element name is hidden
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the iframe is hidden</li>
     * <li>I verify the document link is not hidden</li>
     * <li>I verify the data for the user's current plan is not hidden</li>
     * </ul>
     * @param elementName String the name of the element
     * @param assertion String "visible" for a positive check, anything else for negative
     */
    @Then("^I verify (?:the|a|an) (.*?) is (visible|hidden)$")
    public static void verifyElementVisibility(String elementName, String assertion) {
        String expectedResult = SentinelStringUtils.format("Expected the element {} to be {}.", elementName, assertion);
        if (assertion.contentEquals("visible")) {
        	assertTrue(expectedResult, getElement(elementName).isDisplayed());
        } else {
        	assertTrue(expectedResult, getElement(elementName).isInvisible());
        }
    }
    
    /**
     * Redirects to the given pageName
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I am redirected to the home page</li>
     * <li>I am redirected to the login page</li>
     * <li>I am shown the member portal pop-up overlay</li>
     * </ul>
     * @param pageName String the name of the place to redirect to
     * @throws InterruptedException if the thread gets interrupted while sleeping
     */
    @Then("^I am (?:redirected to|shown) the (.*) (?:(?:P|p)age|(?:O|o)verlay)$")
    public static void redirectedToPage(String pageName) throws InterruptedException {
        pageName = pageName.replaceAll("\\s", "") + "Page";
        PageManager.setPage(pageName);
        PageManager.waitForPageLoad();
    }
    
    /**
     * Identifies the first iFrame in a document and switches to it.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I enter the iFrame
     * </ul>
     * @see com.dougnoel.sentinel.pages.PageManager#switchToIFrame()
     */
    @Then("^I enter the iFrame$")
    public static void switchToIFrame() {
        PageManager.switchToIFrame();
    }
    
    /**
     * Exits iFrame.
     * <p>
     * <b>Gherkin Example:</b>
     * <ul>
     * <li>I exit the iFrame</li>
     * </ul>
     */
    @Then("^I exit the iFrame$")
    public static void exitIFrame() {
        PageManager.exitIFrame();
    }
}