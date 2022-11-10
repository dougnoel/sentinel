package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static org.junit.Assert.*;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.math.Decimal;
import org.apache.commons.lang3.StringUtils;

import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.openqa.selenium.Point;
import java.math.BigDecimal;

/**
 * Methods used to defined basic validations
 *
 */
public class VerificationSteps {
    /**
     * <p>
     * Used to compare the numerical text, or input element value, of an element to a stored value to verify how much it
     * has changed given a set number of decimal places truncated or rounded up/down
     * Can verify whether a value is greater than or less than the stored by a given amount.
     * </p><p>
     * Only numerical values will correctly work with this verification step.
     * Supports decimals, rounding up/down (half up), and truncation padded to a given decimal length.
     * </p><p>
     * Examples:
     * <ul>
     * <li>Compare 0.0010 is equivalent to later displayed -0.0011 due to another operation</li>
     * <li>Compare 0.0010 is equivalent to later displayed 0.0011 due to another operation</li>
     * <li>Compare 5.1-2 is equivalent to displayed 3.1</li>
     * <li>Compare 5-2 is equivalent to displayed 3</li>
     * <li>Compare 5-2 is equivalent to displayed 3.00</li>
     * </ul>
     * </p><p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the value of the <b>raw text div</b> is <b>.3005</b> <b>greater</b> than the old value of the <b>raw text div</b></li>
     * <li>I verify the value of the <b>raw text div</b> is <b>.1</b> <b>less</b> than the old value  of the <b>raw text div</b></li>
     * <li>I verify the value of the <b>rounded text div</b> is <b>3</b> <b>greater</b> than the old value of the <b>raw text div</b> <b>rounded</b> to <b>0</b> decimal places</li>
     * <li>I verify the value of the <b>rounded text div</b> is <b>.1</b> <b>less</b> than the old value of the <b>raw text div</b> <b>rounded</b> to <b>1</b> decimal place</li>
     * <li>I verify the value of the <b>truncated text div</b> is <b>3</b> <b>greater</b> than old the value of the <b>raw text div</b> <b>truncated</b> to <b>0</b> decimal places</li>
     * <li>I verify the value of the <b>truncated text div</b> is <b>.1</b> <b>less</b> than the old value of the <b>raw text div</b> <b>truncated</b> to <b>1</b> decimal place</li>
     * </ul></p><p>
     * <b>Scenario Outline Example:</b>
     * I verify the value of the <b>&lt;String&gt;</b> is <b>&lt;Number&gt;</b> <b>&lt;String (greater|less)&gt;</b> than the old value of the <b>&lt;String&gt;</b> <i>&lt;<b>&lt;String (rounded|truncated)&gt;</b> to <b>&lt;Integer&gt;</b> decimal places&gt;</i>
     * </p>
     * @param elementName String the name of the element whose value/text we are fetching
     * @param difference Double how much more/less the new value is from the stored value
     * @param operator String whether we're checking if the new value is more or less than the stored value
     * @param storedValueKey String the key of the stored value
     * @param isRounded String to determine if we're rounding <i>(half-up)</i>, or truncating
     * @param decimalCount String how many decimal places we wish to pad/truncate/round to <i>(0 if null with rounding/truncation specified)</i>
     */
    @Then("^I verify the value of the (.*?) is (.*?) (greater|less) than the old value of the (.*?)( (rounded|truncated) to )?(([0-9]+) decimal places?)?$")
    public static void verifyNumDiffFixedDecimals(String elementName, double difference, String operator, String storedValueKey, String isRounded, String decimalCount) {
        String unparsedStoredValue = null;
        String actual = null;

        try{
            unparsedStoredValue = Configuration.toString(storedValueKey);

            if(unparsedStoredValue == null) {
                String storedValueMissingError = SentinelStringUtils.format("No stored value was found for {}. Values can be stored with preceding steps.",
                        storedValueKey);

                throw new NumberFormatException(storedValueMissingError);
            }

            actual = getElement(elementName).getAttribute("value");
            if(actual == null)
                actual = getElement(elementName).getText();

            double storedValue = Double.parseDouble(unparsedStoredValue);
            BigDecimal expectedValue;
            if (operator.contentEquals("greater"))
                expectedValue = BigDecimal.valueOf(storedValue + difference);
            else
                expectedValue = BigDecimal.valueOf(storedValue - difference);

            String expected = expectedValue.stripTrailingZeros().toString();
            if(isRounded != null) {
                int parsedDecimalCount = 0;
                boolean rounded = true;

                if(decimalCount != null)
                    parsedDecimalCount = Integer.parseInt(decimalCount);
                if(isRounded.contains("truncated"))
                    rounded = false;

                expected = Decimal.formatDecimal(expectedValue, parsedDecimalCount, rounded);
            }

            String expectedResult = SentinelStringUtils.format("Expected the value of the {} element to be {}. {} {} than the stored value {} {}{} decimal places. The found value was instead {}.",
                    elementName, expected, difference, operator, unparsedStoredValue,
                    (isRounded != null ? "with " : isRounded + " to "),
                    (decimalCount != null ? "unspecified" : decimalCount),
                    actual);

            Assert.assertEquals(expectedResult, expected, actual);

        } catch (NumberFormatException parseFailure) {
            if(unparsedStoredValue == null)
                throw parseFailure;
            else {
                String numberError = SentinelStringUtils.format("Expected numerical values, but the stored value was {}, and the element value was {}",
                        unparsedStoredValue, actual);
                throw new NumberFormatException(numberError);
            }
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
     * Verifies an attribute for an element has a given previously stored value.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the boxes table with the attribute color has the same value used for the blue box</li>
     * <li>I verify the New Div with the attribute style does not have the same value used for the font style</li>
     * <li>I verify the Title Text with the attribute class contains the same value stored as the title text entry</li>
     * <li>I verify the Submit Button with the attribute class does not contain the same value stored for the disabled attribute</li>
     * </ul>
     * @param elementName String element to inspect
     * @param assertion String "has" for a positive check, anything else for negative
     * @param attribute String attribute to inspect
     * @param key String key of the stored value to expect
     */
    @Then("^I verify the (.*?) attribute of the (.*?)( does not)? match(?:es)? the (.*?)$")
    public static void verifyElementAttributeHasStoredValue(String attribute, String elementName, String assertion, String key) {
        String valueToVerify = Configuration.toString(key);
        String errorMessage = SentinelStringUtils.format("Could not find a text value stored with the {} key", key);
        Assert.assertNotNull(errorMessage, valueToVerify);

        verifyElementAttributeHasValue(elementName, attribute, assertion, "has", valueToVerify);
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

        String actual = getElement(elementName).getAttribute(attribute);
        String expectedResult = SentinelStringUtils.format("The element \"{}\" with the attribute \"{}\" was expected to{} {} the value {}. The attribute had the value \"{}\".",
                elementName, attribute, (negate ? " not" : ""),
                matchType.replace("has", "have").replace("contains", "contain"), value, actual);

        boolean matchFound;
        if (partialMatch) {
            if(attribute.equals("class"))
                matchFound = getElement(elementName).classContains(value);
            else
                matchFound = getElement(elementName).attributeContains(attribute, value);
        } else
            matchFound = getElement(elementName).attributeEquals(attribute, value);

        if (negate)
            assertFalse(expectedResult, matchFound);
        else
            assertTrue(expectedResult, matchFound);
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
    	var actualText = PageManager.getPage().getJsAlertText();
        String expectedResult = SentinelStringUtils.format("Expected the JS alert to {}contain the text {}. The alert contained the text {}.", negate ? "not ": "", expectedText, actualText);
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