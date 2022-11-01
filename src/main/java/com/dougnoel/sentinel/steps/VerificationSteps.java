package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static org.junit.Assert.*;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.NumberFormatException;
import org.apache.commons.lang3.StringUtils;

import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.Point;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Methods used to defined basic validations
 *
 */
public class VerificationSteps {

    /**
     * Used to compare the numerical text, or input element value, of an element to a stored value to verify how much it has changed
     * assuming no rounding/truncation is required.
     * Can verify whether a value is greater than or less than the stored by a given amount.
     * <p>
     * Only numerical values will correctly work with this verification step.
     * <p>
     * Supports decimals.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the numerical <b>value</b> of the <b>added number result input</b> element is <b>3</b> <b>less</b> than the previously stored <b>first number input</b> value</li>
     * <li>I verify the numerical <b>text</b> of the <b>added number result span</b> element is <b>3</b> <b>greater</b> than the previously stored <b>first number input</b> value</li>
     * <li>I verify the numerical <b>text</b> of the <b>added number result span</b> element is <b>3.5</b> <b>greater</b> than the previously stored <b>first decimal input</b> value</li>
     * </ul>
     * <b>Scenario Outline Example:</b>
     * <p>
     * I verify the numerical &lt;checking input or text value&gt of the &lt;element&gt element is &lt;numerical difference&gt &lt;less or more&gt than the previously &lt;stored or used&gt &lt;variable key&gt value
     * <p>
     * @param isInput String determine if we're fetching an input's value, or an element's numerical text to compare
     * @param elementName String the name of the element whose value/text we are fetching
     * @param difference Double how much more/less the new value is from the stored value
     * @param operator String whether we're checking if the new value is more or less than the stored value
     * @param storedValueKey String the key of the stored value
     */
    @Then("^I verify the numerical (value|text) of the (.*?) element is (.*?) (greater|less) than the previously (?:stored|used) (.*?) value$")
    public static void verifyFullNumDifference(String isInput, String elementName, double difference, String operator, String storedValueKey) {
        verifyNumDiffFixedDecimals(isInput, elementName, difference, operator, storedValueKey, null, -1);
    }

    /**
     * Used to compare the numerical text, or input element value, of an element to a stored value to verify how much it
     * has changed given a set number of decimal places truncated or rounded up/down
     * Can verify whether a value is greater than or less than the stored by a given amount.
     * <p>
     * Only numerical values will correctly work with this verification step.
     * <p>
     * Supports decimals, rounding up/down, and truncation padded to a given decimal length:
     * <p>
     * Examples:
     * <ul>
     * <li>Compare 0.0010 is equivalent to later displayed 0.0011 due to another operation</li>
     * <li>Compare 2.00125-1.0001 rounded up to 0 decimal places is equivalent to displayed 1</li>
     * <li>Compare 2.50125-1.0001 rounded up to 0 decimal places is equivalent to displayed 2</li>
     * <li>Compare 2.50125-1.0001 truncated to 0 decimal places is equivalent to displayed 1</li>
     * <li>Compare 0.00125-0.0001 rounded up to 4 decimal places is equivalent to displayed 0.0012</li>
     * <li>Compare 0.00125-0.0001 rounded down to 4 decimal places is equivalent to displayed 0.0011</li>
     * <li>Compare 0.00125-0.0002 truncated to 4 decimal places is equivalent to displayed 0.0010</li>
     * </ul>
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the numerical <b>value</b> of the <b>added number result input</b> element is <b>3</b> <b>less</b> than the previously stored <b>first number input</b> value rounded up to 2 decimal places</li>
     * <li>I verify the numerical <b>text</b> of the <b>added number result span</b> element is <b>3</b> <b>greater</b> than the previously stored <b>first number input</b> value rounded down to 3 decimal places</li>
     * <li>I verify the numerical <b>text</b> of the <b>added number result span</b> element is <b>3.5</b> <b>greater</b> than the previously stored <b>first decimal input</b> value truncated to 3 decimal places</li>
     * </ul>
     * <b>Scenario Outline Example:</b>
     * <p>
     * I verify the numerical &lt;checking input or text value&gt of the &lt;element&gt element is &lt;numerical difference&gt &lt;less or more&gt than the previously &lt;stored or used&gt &lt;variable key&gt value &lt;rounding or truncating&gt to &lt;number of decimal places&gt decimal places
     * <p>
     * @param isInput String determine if we're fetching an input's value, or an element's numerical text to compare
     * @param elementName String the name of the element whose value/text we are fetching
     * @param difference Double how much more/less the new value is from the stored value
     * @param operator String whether we're checking if the new value is more or less than the stored value
     * @param storedValueKey String the key of the stored value
     * @param isRounded String to determine if we're rounding up, down, or truncating
     * @param decimalCount Integer how many decimal places we wish to pad/truncate/round to
     */
    @Then("^I verify the numerical (value|text) of the (.*?) is (.*?) (greater|less) than the previously (?:stored|used) (.*?) value (rounded|truncated) to ([0-9]+) decimal places$")
    public static void verifyNumDiffFixedDecimals(String isInput, String elementName, double difference, String operator, String storedValueKey, String isRounded, int decimalCount) {
        String unparsedStoredValue = null;
        String actual = null;

        try{
            unparsedStoredValue = Configuration.toString(storedValueKey);

            if(unparsedStoredValue == null) {
                String storedValueMissingError = SentinelStringUtils.format("No stored value was found with the key {}",
                        storedValueKey);

                throw new NumberFormatException(storedValueMissingError);
            }

            if (isInput.contentEquals("value"))
                actual = getElement(elementName).getAttribute("value");
            else
                actual = getElement(elementName).getText();

            double storedValue = Double.parseDouble(unparsedStoredValue);
            double expectedValue;
            if (operator.contentEquals("greater"))
                expectedValue = storedValue + difference;
            else
                expectedValue = storedValue - difference;

            String expected = String.valueOf(expectedValue);
            if(isRounded != null) {
                String leadingFormat = "#";
                if(decimalCount > 0)
                    leadingFormat+=".";
                String decimalPlaces = "#".repeat(decimalCount);

                DecimalFormat formatOutput = new DecimalFormat(leadingFormat + decimalPlaces);

                switch(isRounded){
                    case "rounded":
                        break;
                    case "truncated":
                        formatOutput.setRoundingMode(RoundingMode.DOWN);
                        break;
                    default:
                        break;
                }

                expected = formatOutput.format(expectedValue);
            }

            String expectedResult = SentinelStringUtils.format("Expected the numerical {} of the \"{}\" element to be \"{}\". {} {} than the stored value {}. The found value was instead \"{}\".",
                    isInput, elementName, expected, difference, operator, storedValue, actual);

            Assert.assertEquals(expectedResult, expected, actual);

        } catch (java.lang.NumberFormatException parseFailure) {
            String numberError = SentinelStringUtils.format("Expected numerical values, but the stored value was \"{}\", and the element contained \"{}\"",
                    unparsedStoredValue, actual);

            throw new NumberFormatException(numberError, parseFailure);
        }
    }

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
     * <li>I verify the div contains the class aria</li>
     * <li>I verify the button does not contain the class disabled</li>
     * </ul>
     * @param elementName String element to inspect
     * @param assertion String "has" for a positive check, anything else for negative
     * @param matchType String whether we are doing an exact match or a partial match
     * @param className String class to verify
     */
    @Then("^I verify (?:the|a|an) (.*?)( does not)? (has|have|contains?) the class (.*?)$")
    public static void verifyElementHasClass(String elementName, String assertion, String matchType, String className) {
        verifyElementAttributeHasValue(elementName, "class", assertion, matchType, className);
    }

    /**
     * Verifies an attribute for an element has a given value.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the boxes table with the attribute color has the value blue"</li>
     * <li>I verify the New Div with the attribute style does not have the value aria"</li>
     * <li>I verify the Title Text with the attribute class contains the value large-12</li>
     * <li>I verify the Submit Button with the attribute class does not contain the value disabled</li>
     * </ul>
     * @param elementName String element to inspect
     * @param assertion String "has" for a positive check, anything else for negative
     * @param matchType String whether we are doing an exact match or a partial match
     * @param attribute String attribute to inspect
     * @param value String value expected
     */
    @Then("^I verify (?:the|a|an) (.*?) with (?:the|a|an) attribute (.*?)( does not)? (has|have|contains?) (?:the|a|an) value (.*?)$")
    public static void verifyElementAttributeHasValue(String elementName, String attribute, String assertion, String matchType, String value) {
        boolean negate = !StringUtils.isEmpty(assertion);
        boolean partialMatch = matchType.contains("contain");
        String actualValue;
        String expectedResult;

        if (attribute.equals("class") && partialMatch) {
            actualValue = getElement(elementName).getAttribute("class");
            expectedResult = SentinelStringUtils.format("Expected the element \"{}\" to{} {} the class \"{}\". Had the actual class(es) \"{}\".",
                    elementName, (negate ? " not" : ""), matchType.replace("has", "have").replace("contains", "contain"), value, actualValue);

            if (negate) {
                assertFalse(expectedResult, getElement(elementName).classContains(value));
            } else {
                assertTrue(expectedResult, getElement(elementName).classContains(value));
            }
        }
        else{
            actualValue = getElement(elementName).getAttribute(attribute);
            expectedResult = SentinelStringUtils.format("The element \"{}\" with the attribute \"{}\" was expected to{} {} the value {}. The attribute had the value \"{}\".",
                    elementName, attribute, (negate ? " not" : ""), matchType.replace("has", "have").replace("contains", "contain"), value, actualValue);

            if (partialMatch) {
                if (negate) {
                    assertFalse(expectedResult, getElement(elementName).attributeContains(attribute, value));
                } else {
                    assertTrue(expectedResult, getElement(elementName).attributeContains(attribute, value));
                }
            } else {
                if (negate) {
                    assertFalse(expectedResult, getElement(elementName).attributeEquals(attribute, value));
                } else {
                    assertTrue(expectedResult, getElement(elementName).attributeEquals(attribute, value));
                }
            }
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
        	assertTrue(expectedResult, getElement(elementName).isHidden());
        }
    }

    /**
     * Verifies the existence of a Javascript alert.
     * <p>
     * <b>Gherkin Example:</b>
     * <ul>
     * <li>I verify the JS alert is present</li>
     * <li>I verify a JS alert is not present</li>
     * </ul>
     * @param assertion String any string for a negative check, nothing (null) for a positive check
     */
    @Then("^I verify (?:the|a) JS alert is( not)? present$")
    public static void verifyJsAlertPresent(String assertion)
    {
    	String expectedResult = SentinelStringUtils.format("Expected a JS alert to be{} present.", assertion);
    	var actualResult = PageManager.getPage().isJsAlertPresent();
        if (assertion != null) {
        	assertFalse(expectedResult, actualResult);
        } else {
        	assertTrue(expectedResult, actualResult);
        }
    }

    /**
     * Verifies the existence of a Javascript alert.
     * <p>
     * <b>Gherkin Example:</b>
     * <ul>
     * <li>I verify the JS alert contains the text Hello, World!</li>
     * <li>I verify the JS alert does not contain the text Goodbye, World!</li>
     * </ul>
     * @param assertion String any string for a negative check, nothing (null) for a positive check
     */
    @Then("^I verify the JS alert (does not )?contains? the text (.*)?$")
    public static void verifyJsAlertText(String assertion, String expectedText)
    {
    	boolean negate = !StringUtils.isEmpty(assertion);
    	String expectedResult = SentinelStringUtils.format("Expected the JS alert to {}contain the text {}.", negate ? "not ": "", expectedText);
    	var actualText = PageManager.getPage().getJsAlertText();
    	boolean result = actualText.contains(expectedText);
    	assertTrue(expectedResult, result != negate);
    }

    @Then("^I verify the (.*) is (to the right of|to the left of|below|above) the (.*)$")
    public static void verifyElementDisplayOrderOnPage(String element1, String relativeLocation, String element2){
        Point element1point = getElement(element1).getLocation();
        Point element2point = getElement(element2).getLocation();

        String expectedResult = SentinelStringUtils.format("Expected the {} to be {} the {}.", element1, relativeLocation, element2);

        if(relativeLocation.contains("above")){
            assertTrue(expectedResult, element1point.y < element2point.y);
        }
        else if(relativeLocation.contains("below")){
            assertTrue(expectedResult, element1point.y > element2point.y);
        }
        else if(relativeLocation.contains("right")){
            assertTrue(expectedResult, element1point.x > element2point.x);
        }
        else{
            assertTrue(expectedResult, element1point.x < element2point.x);
        }

    }

}