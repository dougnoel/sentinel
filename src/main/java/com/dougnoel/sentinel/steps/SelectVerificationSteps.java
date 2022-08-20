package com.dougnoel.sentinel.steps;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;

import com.dougnoel.sentinel.elements.dropdowns.SelectElement;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import io.cucumber.java.en.Then;

public class SelectVerificationSteps {

    /**
     * Verifies a select has an option with the given text.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the size dropdown has the option XS</li>
     * <li>I verify the color dropdown does not have the option Red</li>
     * </ul>
     * @param elementName String select element to check
     * @param assertion String "has" for a positive check, anything else for negative
     * @param textOfOption String text to match against
     */
    @Then("^I verify (?:the|a|an) (.*?) (has|does not have) the option (.*?)$")
	public static void verifyDropdownHasOption(String elementName, String assertion, String textOfOption) {
		var dropdown = (SelectElement)getElement(elementName);
		
		String expectedResult = SentinelStringUtils.format("Expected the element {} {} the option \"{}\".",
                elementName, assertion, textOfOption);
		if (assertion.contentEquals("has")) {
            assertTrue(expectedResult, dropdown.hasOption(textOfOption));
        } else {
            assertTrue(expectedResult, dropdown.doesNotHaveOption(textOfOption));
        }
	}
	
    /**
     * Verifies a select element's currently selected option has the given text.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the size dropdown has the currently selected option XS</li>
     * <li>I verify the color dropdown does not have the currently selected option Red</li>
     * </ul>
     * @param elementName String select element to check
     * @param assertion String "has" for a positive check, anything else for negative
     * @param textOfOption String text to match against
     */
    @Then("^I verify (?:the|a|an) (.*?) (has|does not have) the currently selected option (.*?)$")
	public static void verifyDropdownHasCurrentlySelectedOption(String elementName, String assertion, String textOfOption) {
		var dropdown = (SelectElement)getElement(elementName);
		
		String expectedResult = SentinelStringUtils.format("Expected the element {} {} to have the currently selected option \"{}\".",
                elementName, assertion, textOfOption);
		var actualResult = dropdown.getSelectedText().equals(textOfOption);
		if (assertion.contentEquals("has")) {
            assertTrue(expectedResult, actualResult);
        } else {
            assertFalse(expectedResult, actualResult);
        }
	}
    
    /**
     * Verifies a select element has or does not have the given number of options.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the size dropdown has 1 option</li>
     * <li>I verify the color dropdown does not have 2 options</li>
     * </ul>
     * @param elementName String select element to check
     * @param assertion String "has" for a positive check, anything else for negative
     * @param numberOfOptions int the number of options to verify
     */
    @Then("^I verify (?:the|a|an) (.*?) (has|does not have) (\\d+) options?$")
    public static void verifyDropdownHasNumberOfOptions(String elementName, String assertion, int numberOfOptions) {
    	var dropdown = (SelectElement)getElement(elementName);
    	var negate = !assertion.contains("has");
        var actualOptionCount = dropdown.getNumberOfOptions();
    	String expectedResult = SentinelStringUtils.format("Expected the element {} to {}have {} option{}, but had {} option{}",
                elementName, (negate ? "not " : ""), numberOfOptions, (numberOfOptions > 1 ? "s" : ""), actualOptionCount, (actualOptionCount > 1 ? "s" : ""));
		var actualResult = actualOptionCount == numberOfOptions;
    	
    	if (negate) {
            assertFalse(expectedResult, actualResult);
        } else {
            assertTrue(expectedResult, actualResult);
        }
    }
}