package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static com.dougnoel.sentinel.elements.ElementFunctions.getElementAsSelectElement;
import static com.dougnoel.sentinel.elements.ElementFunctions.getElementAsTable;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.filemanagers.DownloadManager;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.StringUtils;
import com.dougnoel.sentinel.webdrivers.WebDriverFactory;

import cucumber.api.java.en.Then;

/**
 * Methods used to defined basic validations
 *
 */
public class VerificationSteps {
	
    private static final Logger log = LogManager.getLogger(VerificationSteps.class.getName()); // Create a logger.
	
	  /**
     * Verifies we have the expected, given number of rows in the given string representing the Table object.
 	 * The string is made lower case and whitespaces are replaced with underscores, then it is sent a 
 	 * getNumberOfRows event and returns the number of rows in the table. The page object and driver 
 	 * object are defined by the WebDriverFactory and PageFactory objects. The derived Page Object (extends
     * Page) should define a method named [element name]_[element type] returning a Table object (e.g. results_table).
     * <p>
     * <b>NOTE:</b> Headers defined with a &lt;th&gt; tag will not be counted. If a
     * row has a &lt;tr&gt; tag and a class of "header", it will be counted.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I see 10 rows in the results table</li>
     * <li>I see 20 rows in the User List Table</li>
     * <li>I see 5 rows in the Current Year Documents table</li>
     * </ul>
     * 
     * @param expectedNumberOfRows int The number of rows your are expecting.
     * @param elementName String (Table name) This should be the name of the table element to count.
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Then("^I see (\\d+) rows in the (.*)$")
    public static void i_see_x_rows_in_a_table(int expectedNumberOfRows, String elementName) throws Throwable {
        int numberOfRows = (int) getElementAsTable(elementName).getNumberOfRows();
        String expectedResult = StringUtils.format("Expected {} rows, found {} rows.", expectedNumberOfRows, numberOfRows);
        assertTrue(expectedResult, numberOfRows == expectedNumberOfRows);
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
     * I verify the &lt;element&gt; &lt;Assertion&gt; exist
     * <p>
     * Examples:
     * &lt;table summary="Examples"&gt;
     * &lt;tr&gt;
     * &lt;td&gt;| element&lt;/td&gt;
     * &lt;td&gt;| Assertion&lt;/td&gt;
     * &lt;td&gt;|&lt;/td&gt;
     * &lt;/tr&gt;
     * &lt;tr&gt;
     * &lt;td&gt;| first dropdown&lt;/td&gt;
     * &lt;td&gt;| does&lt;/td&gt;
     * &lt;td&gt;|&lt;/td&gt;
     * &lt;/tr&gt;
     * &lt;tr&gt;
     * &lt;td&gt;| second dropdown&lt;/td&gt;
     * &lt;td&gt;| does not&lt;/td&gt;
     * &lt;td&gt;|&lt;/td&gt;
     * &lt;/tr&gt;
     * &lt;/table&gt;
     * 
     * @param elementName String Element to check
     * @param assertion String "does" or does not" for true or false
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Then("^I verify (?:the|a|an) (.*?)( does not)?(?: does)? exists?$")
    public static void i_verify_an_element_exists(String elementName, String assertion) throws Throwable {
        boolean negate = !StringUtils.isEmpty(assertion);
        String expectedResult = StringUtils.format("Expected the element {} to {}exist.",
                elementName, (negate ? "not " : ""));
        if (negate) {
            // We need a different assertion here because checking to see if something does
            // exist takes 10 seconds to come back with a failure when we want it to come
            // back much faster.
            assertTrue(expectedResult, getElement(elementName).doesNotExist());
        } else {
            assertTrue(expectedResult, getElement(elementName).isDisplayed());
        }
    }
    
    /**
     * Verifies an element has an attribute by asserting the element for the given elementName has the given class attribute.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the table has the attribute "table-striped"</li>
     * <li>I verify the textbox does not have the attribute "listStyleClass"</li>
     * <li>I verify the div has the attribute "container"</li>
     * </ul>
     * @param elementName String element to inspect
     * @param assertion String if not null we expect this be true
     * @param attribute String class attribute to verify
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Then("^I verify (?:the|a|an) (.*) (?:has)?(does not have)? the attribute (.*)$")
    public static void i_verify_an_element_has_an_attribute(String elementName, String assertion, String attribute)
            throws Throwable {
        boolean negate = !StringUtils.isEmpty(assertion);
        String expectedResult = StringUtils.format("Expected the element {} to {}have the attribute \"{}\".",
                elementName, (negate ? "" : "not "), attribute);
        log.trace(expectedResult);
        if (negate) {
            assertFalse(expectedResult, getElement(elementName).hasClass(attribute));
        } else {
            assertTrue(expectedResult, getElement(elementName).hasClass(attribute));
        }
    }
    
    /**
     * Verifies an element is active by asserting the element for the given element name has class attribute 'active'
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the menu item for the current page is active</li>
     * <li>I verify the second li element in the accordion is active</li>
     * <li>I verify the password field is not active</li>
     * </ul>
      * @param elementName String  the element to inspect for the active class
     * @param assertion String if not null we expect this be true
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Then("^I verify (?:the|a|an) (.*) is( not)? active$")
    public static void i_verify_an_element_is_active(String elementName, String assertion) throws Throwable {
        boolean negate = !StringUtils.isEmpty(assertion);
        String expectedResult = StringUtils.format("Expected the element {} to {}be active.",
                elementName, (negate ? "" : "not "));
        log.trace(expectedResult);
        if (negate) {
            assertFalse(expectedResult, getElement(elementName).hasClass("active"));
        } else {
            assertTrue(expectedResult, getElement(elementName).hasClass("active"));
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
     * @param assertion String if not null we expect this be true
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Then("^I verify (?:the|a|an) (.*?) is( not)? enabled$")
    public static void i_verify_an_element_is_enabled(String elementName, String assertion) throws Throwable {
        boolean negate = !StringUtils.isEmpty(assertion);
        String expectedResult = StringUtils.format("Expected the element {} to {}be enabled.",
                elementName, (negate ? "not " : ""));
        log.trace(expectedResult);
        int waitTime = (negate) ? 1 : 10; // If we expect it to fail, only wait a second, otherwise wait the normal 10
                                          // seconds
        assertTrue(expectedResult, negate != getElement(elementName).isEnabled(waitTime));
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
     * @param assertion String if the assertion is not empty we expect it to be hidden
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Then("^I verify (?:the|a|an) (.*?) is( not)? hidden$")
    public static void i_verify_an_element_is_hidden(String elementName, String assertion) throws Throwable {
        boolean negate = !StringUtils.isEmpty(assertion); // is hidden = empty, so negate is false
        String expectedResult = StringUtils.format("Expected the element {} to be {}.", elementName, (negate ? "visible"
                : "hidden"));
        log.debug(expectedResult);
        int waitTime = (negate) ? 10 : 1; // If we expect it to fail, only wait a second, otherwise wait the normal 10
                                          // seconds
        assertTrue(expectedResult, negate == getElement(elementName).isDisplayed(waitTime));
    }
    
    /**
     * Verifies the element has text by asserting item contains text
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the title header is not empty</li>
     * <li>I verify the textbox is empty</li>
     * <li>I verify the Provider dropdown is not empty</li>
     * </ul>
     * @param elementName String name of the element to verify.
     * @param assertion Sting if this is not empty, we expect it to be true.
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Then("^I verify (?:the|a|an) (.*?)( is not)?(?: is)? empty?$")
    public static void i_verify_an_element_text(String elementName, String assertion) throws Throwable {
        boolean negate = !StringUtils.isEmpty(assertion);
        String expectedResult = StringUtils.format("Expected the element {} to {}be empty.",
                elementName, (negate ? "not " : ""));
        if (negate) {
            assertFalse(expectedResult, getElement(elementName).getText().isEmpty());
        } else {
            assertTrue(expectedResult, getElement(elementName).getText().isEmpty());
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
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Then("^I am (?:redirected to|shown) the (.*) (?:(?:P|p)age|(?:O|o)verlay)$")
    public static void i_am_redirected_to_the_page(String pageName) throws Throwable {
        pageName = pageName.replaceAll("\\s", "") + "Page";
        PageManager.setPage(pageName);
        // TODO: Add code to verify the page has loaded.
        // Add a wait for now.
    }
    
    /**
     * Opens the given pageName in a new window
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I see a new tab open with the Home page</li>
     * <li>I see a new tab open with the Login page</li>
     * <li>I see a new tab open with the Google Maps page</li>
     * </ul>
     * @param pageName String the page to open
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Then("^I see a new tab open with the (.*) Page$")
    public static void i_see_a_new_tab_open_with_the_foo_Page(String pageName) throws Throwable {
        PageManager.switchToNewWindow();
        pageName = pageName.replaceAll("\\s", "") + "Page";
        PageManager.setPage(pageName);
        // TODO: Add code to verify the page has loaded.
    }
    
    
    /**
     * Validates whether the currently open pdf contains the given text on the page(s) passed to it.
     * If a page number is passed for the first page and a null is passed for the second page, then 
     * only that page is searched. If two page numbers are passed, then the entire range is searched,
     * inclusive of the two page numbers given.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>	Then I see the text Validation Text appears on the 3rd page of the pdf</li>
     * <li>	Then I see the text 1Q2W3E_4R5T appears between the 1st and 4th pages of the pdf</li>
     * </ul>
     * <b>NOTE:</b> This will fail if the browser window is not currently open with a pdf loaded.
     * Should be executed after the i_open_the_document_with_extension_in_a_new_tab(linkName,extension) method
     * is called. The cucumber step is: @When("^I open the (.*) (pdf) in a new tab$")
     * <p>
     * @param text_to_verify String the text string expected to appear in the currently open PDF file
     * @param firstPageNumber int the page to check or the first page in the range if the second value is not null
     * @param lastPageNumber Integer the last page in the range; indicates to only check one page if set to null
     * @throws Throwable if any errors are raised they will fail the current test
     */
    @Then("^I see the text (.*) appears (?:on|between) the (\\d+)(?:st|nd|rd|th)(?: and )?(\\d+)?(?:st|nd|rd|th)? pages? of the pdf$")
    public static void i_see_the_text_appears_on_pages_of_the_pdf(String text_to_verify, int firstPageNumber, Integer lastPageNumber) throws Throwable {
            URL url = new URL(PageManager.getCurrentUrl());
            assertTrue(url.toString().contains(".pdf"));
            if (lastPageNumber == null) {
            	assertTrue(DownloadManager.verifyPDFContent(url, text_to_verify, firstPageNumber));	
            } else {
            	assertTrue(DownloadManager.verifyPDFContent(url, text_to_verify, firstPageNumber, lastPageNumber));
            }
    }

    /**
     * Compares the current page we are on with the page stored in
     * memory given the page number and Table element object page for the current page.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I should be shown the 1st page of results from the members search</li>
     * <li>I should be shown the 2nd page of results from the teams search</li>
     * <li>I should be shown the 4th page of results from the pharmacy search</li>
     * </ul>
     * @param pageNumber int Page we are on to use as a key to retrieve data.
     * @param tableName String the name of the table element on the page object
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Then("^I should be shown the (\\d+)(?:st|nd|rd|th) page of results from the (.*)$")
    public static void i_should_be_shown_the_x_page_of_results(int pageNumber, String tableName) throws Throwable {
        assert ((boolean) getElementAsTable(tableName).compareWithStoredTable(pageNumber) == false);
    }
    
    /**
     * Verifies a column contains unique text
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the Date column in the History table contains unique values</li>
     * <li>I verify the Contact column in the Provider table contains unique values</li>
     * <li>I verify the Email column in the Employees table contains unique values</li>
     * </ul>
     * @param columnName String name of the column to verify
     * @param isMultiCells String if table has more than 1 row
     * @param tableName String name of the table to search
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Then("^I verify the (.*?) column(s)? in the (.*?) contains unique values$")
	public static void i_verify_the_column_in_the_table_contains_unique_text(String columnName, String isMultiCells,
			String tableName) throws Throwable {
		if (isMultiCells != null) {		
			assertTrue(getElementAsTable(tableName).verifyRowCellsAreUnique(columnName));
		} else {
			assertTrue(getElementAsTable(tableName).verifyColumnCellsAreUnique(columnName));
		}
	}

    /**
     * Used to verify that an element contains certain text. It takes an element
     * name and then an optional "does not", which if present means that the method
     * will look for the text to not exist. Then it will look for the words has|have
     * to do an exact match or contain(s) to do a partial match. It uses the text
     * contained in double quotes for matching.
     * <p>
     * NOTE: If "URL" (without the quotes) is passed in all caps in place of the
     * element name, This step will check for the text to exist in the current page
     * URL.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the header div contains the text "Header"</li>
     * <li>I verify the state dropdown does not contain the text "VA"</li>
     * <li>I verify the paragraph div has the text "This is my example text."</li>
     * <li>I verify the street name textbox does not have the text "P.O. Box"</li>
     * <li>I verify the url http://google.com has the text "google"</li>
     * </ul>
     * 
     * @param elementName String The name of the element to be evaluated as defined in the page object.
     * @param assertion String Evaluated as a boolean, where null = false and any text = true.
     * @param matchType String whether we are doing an exact match or a partial match
     * @param text String The text to verify exists in the element.
     * @throws Throwable Throws any errors passed to it.
     */
    @Then("^I verify the (.*?)( does not)? (has|have|contains?) the text \"([^\"]*)\"$")
    public static void i_verify_the_element_contains_the_text(String elementName, String assertion, String matchType,
            String text)
            throws Throwable {
        boolean negate = !StringUtils.isEmpty(assertion);
        boolean partialMatch = matchType.contains("contain");
        if (elementName.contains("URL")) {
            i_verify_the_URL_contains_the_text(text);
        } else {
            String elementText = (String) getElement(elementName).getText();
            String expectedResult = StringUtils.format(
                    "Expected the {} element to {}{} the text {}. The element contained the text: {}",
                    elementName, (negate ? "not " : ""), (partialMatch ? "contain" : "exactly match"), text, elementText
                            .replace("\n", " "));
            log.trace(expectedResult);
            if (partialMatch) {
                if (negate) {
                    assertFalse(expectedResult, elementText.contains(text));
                } else {
                    assertTrue(expectedResult, elementText.contains(text));
                }
            } else {
                if (negate) {
                    assertFalse(expectedResult, StringUtils.equals(elementText, text));
                } else {
                    assertTrue(expectedResult, StringUtils.equals(elementText, text));
                }
            }
        }
    }
    /**
     * Verifies the row column has text for the stored value.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the Name column in the user info table contains the text entered text for the username</li>
     * <li>I verify the Contact column in the provider info table contains the text used for the phone number field</li>
     * <li>I verify the Airport Code column in the Airports table contains the text selected for the airport's code RDU</li>
     * </ul>
     * @param columnName String Name of the column to verify
     * @param tableName String Name of the table containing the column
     * @param key String the text to match
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Then("^I verify the (.*?) column in the (.*?) contains the text (?:entered|selected|used) for the (.*)$")
    public static void i_verify_the_row_column_in_the_table_contains_the_text_for_the_stored_value(String columnName,
            String tableName, String key) throws Throwable {
        String textToMatch = ConfigurationManager.getValue(key);
        assertTrue(getElementAsTable(tableName).verifyColumnCellsContain(columnName, textToMatch));
    }
    
    /**
     * Used to verify that the selected option of a select element contains certain
     * text. It takes an element name and then an optional "does not", which if
     * present means that the method will look for the text to not exist. Then it
     * will look for any of the words has|have and uses the text contained in double
     * quotes for matching.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the Area Select Box has the text "This is my example text."
     * selected</li>
     * <li>I verify the cola radio button does not have the text "Root beer"
     * selected</li>
     * </ul>
     * 
     * @param elementName String Name of the Element to verify
     * @param assertion String if empty we expect this to be false
     * @param text String Text to match
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Then("^I verify the (.*?)( does not)? (?:has|have) the text \"([^\"]*)\" selected$")
    public static void i_verify_the_selection_contains_the_text(String elementName, String assertion, String text) throws Throwable {
        boolean negate = !StringUtils.isEmpty(assertion);
        String selectedText = (String) getElementAsSelectElement(elementName).getSelectedText();
        String expectedResult = StringUtils.format(
                "Expected the the selection for the {} element to {}contain the text \"{}\". The element contained the text: \"{}\".",
                elementName, (negate ? "not " : ""), text, selectedText.replace("\n", " "));
        log.trace(expectedResult);
        if (negate) {
            assertFalse(expectedResult, selectedText.contains(text));
        } else {
            assertTrue(expectedResult, selectedText.contains(text));
        }
    }
    
    /**
     * Verifies the given table contains the given column
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the Contact Us table contains the Phone Number column </li>
     * <li>I verify the Vision benefits table contains a Deductible column</li>
     * <li>I verify the Discography table contains the Album Name column </li>
     * </ul>
     * @param tableName String name of the table containing the column
     * @param columnName String name of the column to verify
     * @throws Throwable this exists so that any uncaught exceptions result in the test failing
     */
    @Then("^I verify the (.*?) contains (?:a|the) (.*?) column$")
    public static void i_verify_the_table_contains_the_column(String tableName, String columnName) throws Throwable {
        assertTrue(getElementAsTable(tableName).verifyColumnExists(columnName));
    }
    
    /**
     * Helper function for i_verify_the_element_contains_the_text() for checking
     * text in the URL.
	 *
     * @param text String text to verify in the URL
     * @throws Throwable Throws any errors passed to it.
     */
    private static void i_verify_the_URL_contains_the_text(String text) throws Throwable {
        String currentUrl = PageManager.getCurrentUrl();
        String expectedResult = StringUtils.format("Expected the URL {} to contain the text \"{}\".", currentUrl, text);
        log.trace(expectedResult);
        assertTrue(expectedResult, currentUrl.contains(text));
    }
    
    /**
     * Verifies that a URL loads in a new window when a named link is clicked.|<p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I open the google link in a new tab and verify the url that loads is http://google.com</li>
     * <li>I open the ESPN link in a new tab for verfiy the url is http://go.espn.com</li>
     * <li>I open the Pharmacy Benefits link in a new window and verify the url is https://testsite.com/pharamacy_benefits</li>
     * </ul>
     * @param linkName String The text of the link. NOT a PageElement object name.
     * @param url String the URL expected to be loaded.
     * @throws Throwable Passes through any errors to the executing code.
     */
    @Then("^I open the (.*?) link in a new tab and verify the URL that loads is (.*?)$")
    public static void i_open_the_link_in_a_new_tab_and_verify_the_url(String linkName, String url) throws Throwable {
            WebDriverFactory.getWebDriver().findElement(By.linkText(linkName)).sendKeys(Keys.RETURN);
            PageManager.switchToNewWindow();
            String newUrl = PageManager.getCurrentUrl();
            assertTrue(newUrl.contains(url));
    }
    
    /**
     * Identifies an iframe and switches to it, if it exists.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I should see the Third Party iframe</li>
     * <li>I should see the Provider Search iframe</li>
     * <li>I should see the Facebook iframe</li>
     * </ul>
     * @see com.dougnoel.sentinel.steps.VerificationSteps#i_verify_an_element_exists(String, String)
     * @see com.dougnoel.sentinel.pages.PageManager#switchToIFrame()
     * @param iFrameName String the name of the iframe element on the page object.
     * @throws Throwable Passes through any errors to the executing code.
     */
    @Then("^I should see the (.*) iframe$")
    public static void i_should_see_content_in_the_x_iframe(String iFrameName) throws Throwable {

        // Make sure the content is loaded in the i frame
        VerificationSteps.i_verify_an_element_exists(iFrameName, "");
        PageManager.switchToIFrame();

    }
}