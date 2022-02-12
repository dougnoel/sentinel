package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElementAsSelectElement;

import com.dougnoel.sentinel.configurations.Configuration;
import io.cucumber.java.en.When;

public class SelectSteps {
	
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
     */
    @When("^I select (?!the)(.*?) (?:in|from) the (.*)$")
    public static void selectItemFromElement(String text, String elementName) {
        getElementAsSelectElement(elementName).select(text);
        Configuration.update(elementName, text);
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
     */
    @When("^I select the (\\d+)(?:st|nd|rd|th) option (?:in|from) the (.*)$")
    public static void selectOrdinalItemFromElement(String ordinal, String elementName) {
    	var index = Integer.parseInt(ordinal);
        String text = getElementAsSelectElement(elementName).select(index).getText(index);
        Configuration.update(elementName, text);
    }
}
