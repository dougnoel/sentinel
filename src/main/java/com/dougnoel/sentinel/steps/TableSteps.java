package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElementAsTable;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;

import com.dougnoel.sentinel.configurations.Configuration;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

public class TableSteps {
	
	private static final String XPATH = "xpath";
	private static final String TEXT = "text";
	private static final String CONTAINS_TEXT = ".//*[contains(text(),'";

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
     */
    @When("^I find the (.*?) link in the row of the (.*?) containing the (.*?) value and click it$")
    public static void clickAssociatedLinkInTable(String elementName, String tableName, String key) {
    	var text = Configuration.toString(key);
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
     */
    @When("^I find the (.*?) and click the (text|xpath|value for) (.*?) in the row containing the (text|xpath|value for) (.*?)$")
    public static void clickAssociatedLinkInTable(String tableName, String clickLocatorType, String elementToClick, String matchLocatorType, String elementToMatch) {   	
    	if (StringUtils.equals(clickLocatorType, XPATH) && StringUtils.equals(matchLocatorType, XPATH)) {
			getElementAsTable(tableName).clickElementInRowThatContains(By.xpath(elementToMatch), By.xpath(elementToClick));
		} else if(StringUtils.equals(clickLocatorType, XPATH)) {
			String elementTextToMatch = StringUtils.equals(matchLocatorType, TEXT) ? elementToMatch : Configuration.toString(elementToMatch);
			getElementAsTable(tableName).clickElementInRowThatContains(elementTextToMatch, By.xpath(elementToClick));
		}
    	else if (StringUtils.equals(matchLocatorType, XPATH)) {
			String elementTextToClick = StringUtils.equals(clickLocatorType, TEXT) ? elementToClick : Configuration.toString(elementToClick);
			By matchLocator = By.xpath(elementToMatch);
			getElementAsTable(tableName).clickElementInRowThatContains(matchLocator, elementTextToClick);
		}
		else {
			String elementTextToMatch = StringUtils.equals(matchLocatorType, TEXT) ? elementToMatch : Configuration.toString(elementToMatch);
			String elementTextToClick = StringUtils.equals(clickLocatorType, TEXT) ? elementToClick : Configuration.toString(elementToClick);
			getElementAsTable(tableName).clickElementInRowThatContains(elementTextToMatch, elementTextToClick);
		}
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
     */
    @When("^I find the (\\d+|la)(?:st|nd|rd|th) row in the (.*?) and click the (text|xpath|value for) (.*?)$")
    public static void clickAssociatedLinkInTable(String ordinal, String tableName, String clickLocatorType, String elementToClick) {
    	By clickLocator;
    	int ordinalRow;
    	switch (clickLocatorType) {
    	case XPATH:
    		clickLocator = By.xpath(elementToClick);
    		break;
    	case "value for":
    		elementToClick = Configuration.toString(elementToClick);
    		// If we are retrieving a value we want to fall through here
    	default:
    		clickLocator = By.xpath(CONTAINS_TEXT + elementToClick + "')]");
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
     */
    @Given("^I view the (\\d+)(?:st|nd|rd|th) page of results from the (.*)$")
    public static void storeTableValues(int pageNumber, String tableName) {
        getElementAsTable(tableName).storeTable(pageNumber);
    }
    
    /**
     * Clicks the header element to sort the table on the given column. Does not verify sort direction.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I find and click the header for the Name column in the Users table</li>
     * </ul>
     * @param columnName String the name of the column to click the header of
     * @param tableName String the name of the table element
     */
    @When("^I find and click the header for the (.*) column in the (.*)$")
    public static void clickColumnHeaderToSort(String columnName, String tableName) {
    	getElementAsTable(tableName).clickColumnHeader(columnName);
    }
}
