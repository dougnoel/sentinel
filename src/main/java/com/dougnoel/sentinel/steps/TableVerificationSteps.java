package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElementAsTable;
import static com.dougnoel.sentinel.assertions.TableAssert.*;

import com.dougnoel.sentinel.configurations.Time;
import org.junit.Assert;

import com.dougnoel.sentinel.elements.tables.Table;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import io.cucumber.java.en.Then;
import org.openqa.selenium.By;


public class TableVerificationSteps {
	private static final Logger log = LogManager.getLogger(TableVerificationSteps.class.getName()); // Create a logger.

    private static final String CONTAIN = "contain";
	
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
     * @param tableName String (Table name) This should be the name of the table element to count.
     */
    @Then("^I see (\\d+) rows? in the (.*)$")
    public static void verifyNumberOfTableRows(int expectedNumberOfRows, String tableName) throws Exception {
        Table table = getElementAsTable(tableName);
        String expectedResult = SentinelStringUtils.format("Expected to find {} rows in the table.", expectedNumberOfRows);
        assertEquals(expectedResult, table, expectedNumberOfRows, table::getNumberOfRows);
    }

    /**
     * Compares the current page we are on with the page stored in
     * memory given the page number and Table element object page for the current page.
     * TODO: Add negative case
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I should be shown the 1st page of results from the members search</li>
     * <li>I should be shown the 2nd page of results from the teams search</li>
     * <li>I should be shown the 4th page of results from the pharmacy search</li>
     * </ul>
     * @param pageNumber int Page we are on to use as a key to retrieve data.
     * @param tableName String the name of the table element on the page object
     */
    @Then("^I should be shown the (\\d+)(?:st|nd|rd|th) page of results from the (.*)$")
    public static void compareTables(int pageNumber, String tableName) throws Exception {
        Table table = getElementAsTable(tableName);
    	assertTrue(table, () -> table.compareWithStoredTable(pageNumber));
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
     */
    @Then("^I verify the (.*?) column(s)? in the (.*?) contains? unique values$")
	public static void verifyUniqueColumnText(String columnName, String isMultiCells, String tableName) throws Exception {
        Table table = getElementAsTable(tableName);
		if (isMultiCells != null) {
			assertTrue(table, () -> table.verifyRowCellsAreUnique(columnName));
		} else {
			assertTrue(table, () -> table.verifyColumnCellsAreUnique(columnName));
		}
	}

    /**
     * Verifies the given table contains or exactly matches or does not contain or does not exactly match the given column
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the Contact Us table contains the Phone Number column</li>
     * <li>I verify the Vision benefits table contains a Deductible column</li>
     * <li>I verify the Contact Us table does not contains the Phone Number column</li>
     * <li>I verify the Vision benefits table does not contains a Deductible column</li>
     * <li>I verify the Discography table has the Album Name column</li>
     * <li>I verify the Awards table has a Movie Name column</li>
     * <li>I verify the Discography table does not has the Album Name column</li>
     * <li>I verify the Awards table does not has a Movie Name column</li>
     * <li>I verify the College table have the Course Name column </li>
     * <li>I verify the Expense Report table have a Transport column </li>
     * <li>I verify the Lottery table does not have a Date column</li>
     * <li>I verify the Discography table does not have the Album Name column </li>
     * </ul>
     * @param tableName String name of the table containing the column
     * @param columnName String name of the column to verify
     * @param assertion String if null is passed, looks for match(es), if any strong value is passed, looks for the value to not exist.
     * @param matchType String whether we are doing an exact match or a partial match
     */
    @Then("^I verify the (.*?)( does not)? (has|have|contains?) (?:a|the) (.*?) column$")
    public static void verifyColumnExists(String tableName, String assertion, String matchType, String columnName) throws Exception {
        Table table = getElementAsTable(tableName);
        boolean negate = !StringUtils.isEmpty(assertion);
        String negateText = negate ? "not " : "";
        boolean partialMatch = matchType.contains(CONTAIN);
        String partialMatchText = partialMatch ? CONTAIN : "exactly match";

        String expectedResult = SentinelStringUtils.format("Expected the {} column to {}{} the column header in the {}.",
                columnName, negateText, partialMatchText, tableName);
        if (negate) {
            assertFalse(expectedResult,table, () -> table.verifyColumnHeaderEquals(columnName, partialMatch));
        } else {
            assertTrue(expectedResult, table, () -> table.verifyColumnHeaderEquals(columnName, partialMatch));
        }
    }
    
    /**
     * Verifies a table column has text for the stored value.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the Name column in the user info table contains the same text entered text for the username</li>
     * <li>I verify the Contact column in the provider info table contains the same text used for the phone number field</li>
     * <li>I verify the Airport Code column in the Airports table contains the same text selected for the airport's code RDU</li>
     * </ul>
     * @param columnName String Name of the column to verify
     * @param tableName String Name of the table containing the column
     * @param key String the key to retrieve the text to match from the configuration manager
     */
    @Then("^I verify the (.*?) column in the (.*?) contains the same text (?:entered|selected|used) for the (.*)$")
    public static void verifyStoredTextAppearsInColumn(String columnName, String tableName, String key) throws Exception {
    	var textToMatch = Configuration.toString(key);
        String errorMessage = SentinelStringUtils.format("No previously stored text was found for the \"{}\" key.", key);
        Assert.assertNotNull(errorMessage, textToMatch);

        errorMessage = SentinelStringUtils.format("Expected the {} column of the {} to contain any cells with the text {}", columnName, tableName, textToMatch);
        Table table = getElementAsTable(tableName);
        assertTrue(errorMessage, table, () -> table.verifyAnyColumnCellContains(columnName, textToMatch));
    }
    
    /**
     * Verifies all the cells in a table column does or does not contain the indicated text.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify all the cells in the Zip Code column in the Airports table contain the text 10001</li>
     * <li>I verify all the cells in the Store column in the Coffee Shop Table do not contain the text Roast</li>
     * </ul>
     * @param columnName String Name of the column to verify
     * @param tableName String Name of the table containing the column
     * @param assertion String if null is passed, looks for match(es), if any strong value is passed, looks for the value to not exist.
     * @param textToMatch String the text to look for in the column
     */
    @Then("^I verify all the cells in the (.*?) column in the (.*?)( do(?:es)? not)? (have|has|contains?) the text (.*?)$")
    public static void verifyTextAppearsInColumn(String columnName, String tableName, String assertion, String matchType, String textToMatch) throws Exception {
        boolean negate = !StringUtils.isEmpty(assertion);
        Table table = getElementAsTable(tableName);
        boolean partialMatch = matchType.contains(CONTAIN);
        var expectedResult = SentinelStringUtils.format(
                "Expected the {} column of the {} to {}only contain cells with the text {}.",
                columnName, tableName, (negate ? "not " : ""), textToMatch);
        log.trace(expectedResult);

        if (negate) {
            assertFalse(expectedResult, table, () -> table.verifyAllColumnCellsContain(columnName, partialMatch, textToMatch));
        } else {
            assertTrue(expectedResult, table, () -> table.verifyAllColumnCellsContain(columnName, partialMatch, textToMatch));
        }
    }

    /**
     * Verifies a table column does or does not have a cell that matches the indicated text.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the Name column in the user info table has the text Bill</li>
     * <li>I verify the Result column in the status table does not have the text ERROR</li>
     * <li>I verify the Role column in the user info table contains the text Engineer</li>
     * <li>I verify the State column in the Provider Info Table does not contain the text North Carolina</li>
     * </ul>
     * @param columnName String Name of the column to verify
     * @param tableName String Name of the table containing the column
     * @param assertion String if null is passed, looks for match(es), if any strong value is passed, looks for the value to not exist.
     * @param matchType String whether we are doing an exact match or a partial match
     * @param textToMatch String the text to look for in the column
     */
    @Then("^I verify the (.*?) column in the (.*?)( does not)? (has|have|contains?) the text (.*?)$")
    public static void verifyCellInColumnHasText(String columnName, String tableName, String assertion, String matchType, String textToMatch) throws Exception {
        boolean negate = !StringUtils.isEmpty(assertion);
        boolean partialMatch = matchType.contains(CONTAIN);
        Table table = getElementAsTable(tableName);
        String expectedResult = SentinelStringUtils.format("Expected the {} column of the {} {}{} the text {}.",
                columnName, tableName, (negate ? " does not" : ""), matchType, textToMatch);

        if (partialMatch) {
            if (negate) {
                assertFalse(expectedResult, table, () -> table.verifyAnyColumnCellContains(columnName, textToMatch));
            } else {
                assertTrue(expectedResult, table, () -> table.verifyAnyColumnCellContains(columnName, textToMatch));
            }
        } else {
            if (negate) {
                assertFalse(expectedResult, table, () -> table.verifyAnyColumnCellHas(columnName, textToMatch));
            } else {
                assertTrue(expectedResult, table, () -> table.verifyAnyColumnCellHas(columnName, textToMatch));
            }
        }
    }

    
    /**
     * Verifies a table column's values are sorted in ascending or descending order.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the cells in the First Name column in the Users Table are sorted in ascending order</li>
     * <li>I verify the cells in the Last Name Column in the example table are sorted in descending order</li>
     * <li>I verify the cells in the state column in the Address table are sorted in ascending order</li>
     * </ul>
     * @param columnName String the name of the column to verify
     * @param tableName String the name of the table containing the column
     * @param sortOrder String ascending or descending
     */
    @Then("^I verify the cells in the (.*?) column in the (.*?) are sorted in (ascending|descending) order$")
    public static void verifyColumnSort(String columnName, String tableName, String sortOrder) throws Exception {
        boolean sortAscending = StringUtils.equals(sortOrder, "ascending");
        Table table = getElementAsTable(tableName);
        var expectedResult = SentinelStringUtils.format("Expected the {} column of the {} to be sorted in {} order.", columnName, tableName, (sortAscending ? "ascending" : "descending"));
        log.trace(expectedResult);
        if (sortAscending) {
        	assertTrue(expectedResult, table, () -> table.verifyColumnCellsAreSortedAscending(columnName));
        } else {
            assertTrue(expectedResult, table, () -> table.verifyColumnCellsAreSortedDescending(columnName));
        }
    }

    /**
     * Verifies that a specific cell, given by the row and column, in the table contains the given previously stored text.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the cell in the last row and the Employee First Name column of the Employee table does not contain the same text used for the employee last name field</li>
     * <li>I verify the cell in the 2nd row and the Employee First Name column of the Employee table has the same text used for the employee first name field</li>
     * </ul>
     * @param rowNum String the row number. Can be "la" to specify the last row, or an integer.
     * @param columnName String the name of the column to verify
     * @param tableName String the name of the table containing the column
     * @param assertion String if null is passed, looks for match(es), if any strong value is passed, looks for the value to not exist.
     * @param matchType String whether we are doing an exact match or a partial match
     * @param key String the key of the stored text to look for in the column
     */
    @Then("^I verify the cell in the (la|\\d+)(?:st|nd|rd|th) row and the (.*) column of the (.*?)( do(?:es)? not)? (has|have|contains?) the same text (?:entered|selected|used) for the (.*)$")
    public static void verifyCellInSpecifiedRowAgainstStored(String rowNum, String columnName, String tableName, String assertion, String matchType, String key) throws Exception{
        String textToMatch = Configuration.toString(key);
        String errorMessage = SentinelStringUtils.format("No previously stored text was found for the \"{}\" key.", key);
        Assert.assertNotNull(errorMessage, textToMatch);

        verifyCellInSpecifiedRow(rowNum, columnName, tableName, assertion, matchType, textToMatch);
    }

    /**
     * Verifies that a specific cell, given by the row and column, in the table contains the given text.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the cell in the last row and the Employee First Name column of the Employee table does not contain the text Alice</li>
     * <li>I verify the cell in the 2nd row and the Employee First Name column of the Employee table has the text Bob</li>
     * </ul>
     * @param rowNum String the row number. Can be "la" to specify the last row, or an integer.
     * @param columnName String the name of the column to verify
     * @param tableName String the name of the table containing the column
     * @param assertion String if null is passed, looks for match(es), if any strong value is passed, looks for the value to not exist.
     * @param matchType String whether we are doing an exact match or a partial match
     * @param textToMatch String the text to look for in the column
     */
    @Then("^I verify the cell in the (la|\\d+)(?:st|nd|rd|th) row and the (.*) column of the (.*?)( do(?:es)? not)? (has|have|contains?) the text (.*?)$")
    public static void verifyCellInSpecifiedRow(String rowNum, String columnName, String tableName, String assertion, String matchType, String textToMatch) throws Exception {
    	boolean negate = !StringUtils.isEmpty(assertion);
        Table table = getElementAsTable(tableName);
    	int rowIndex = rowNum.equals("la") ? table.getNumberOfRows() : Integer.parseInt(rowNum);
        boolean partialMatch = matchType.contains(CONTAIN);
    	
    	var expectedResult = SentinelStringUtils.format(
                "Expected the cell in the {} row and the {} column of the {} to {}contain the text {}.",
                SentinelStringUtils.ordinal(rowIndex), columnName, tableName, (negate ? "not " : ""), textToMatch);
    	log.trace(expectedResult);

    	if (negate) {
            assertNotEquals(expectedResult, table, null, () -> table.verifySpecificCellContains(columnName, rowIndex, textToMatch, partialMatch));
        } else {
            assertEquals(expectedResult, table, null, () -> table.verifySpecificCellContains(columnName, rowIndex, textToMatch, partialMatch));
        }
    }

    /**
     * Verifies all cells in the table column are or are not empty.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify all cells in the Zip Code column in the list table are not empty</li>
     * <li>I verify all cells in the Name column in the list table are empty</li>
     * </ul>
     *
     * @param columnName String Name of the column to verify
     * @param tableName  String Name of the table containing the column
     * @param assertion  String if null, checks that all cells are empty. Otherwise, checks that all cells are not empty.
     */
    @Then("^I verify all cells in the (.*?) column in the (.*?) are( not)? empty$")
    public static void verifyColumnIsEmpty(String columnName, String tableName, String assertion) throws Exception {
        boolean negate = !StringUtils.isEmpty(assertion);
        Table table = getElementAsTable(tableName);
        var expectedResult = SentinelStringUtils.format(
                "Expected all cells in the {} column of the {} to {}be empty.",
                columnName, tableName, (negate ? "not " : ""));
        log.trace(expectedResult);

        if (negate) {
            assertTrue(expectedResult, table, () -> table.verifyAllColumnCellsNotEmpty(columnName));
        } else {
            assertTrue(expectedResult, table, () -> table.verifyAllColumnCellsEmpty(columnName));
        }
    }

    @Then("^I verify the (.*?) column in the (.*?) is displayed to the left of the (.*?) column$")
    public static void verifyColumnDisplayOrder(String column1Name, String tableName, String column2Name) throws Exception {
        var expectedResult = SentinelStringUtils.format(
                "Expected the {} column of the {} to be displayed to the left of the {} column.",
                column1Name, tableName, column2Name);
        log.trace(expectedResult);
        Table table = getElementAsTable(tableName);
        assertTrue(expectedResult, table, () -> table.verifyColumnDisplayOrder(column1Name, column2Name));
    }

    /**
     * Verify the element exists in row which have stored value in column name
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify row in the Project Files Table with value selected for the Search Input contains the xpath //i[contains(concat(' ', @class, ' '), ' fa-toggle-on ')]</li>
     * </ul>
     * @param tableName String the name of the table element
     * @param key String the key used to retrieve the value
     * @param xpath String xpath value to element to click
     */
    @Then("^I verify the row in the (.*?) with the value (?:entered|selected|used) for the (.*?)( do(?:es)? not)? contains? the xpath (.*?)$")
    public static void verifyStoredTextRowContainsXpath(String tableName, String key, String assertion, String xpath) throws Exception {
        By locator = By.xpath(xpath);
        boolean negate = !StringUtils.isEmpty(assertion);
        Table table = getElementAsTable(tableName);
        var textToMatch = Configuration.toString(key);
        var expectedResult = SentinelStringUtils.format(
                "Expected the row of the {} to {}contain xpath {}", tableName, (negate ? "not " : ""), xpath);

        if (negate) {
            assertFalse(expectedResult, table, () -> table.verifyRowContains(textToMatch, locator));
        } else {
            assertTrue(expectedResult, table, () -> table.verifyRowContains(textToMatch, locator));
        }
    }

    /**
     * Refreshes the page, waiting the configured time (given by the longProcessTimeout "-DlongProcessTimeout") for the table to contain the given value in the given row and column.
     * By default, the longProcessTimeout is 60 seconds.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I wait for the cell in the 1st row and the Status column of the User table to have the text Disabled</li>
     * <li>I wait for the cell in the last row and the Status column of the ID table to not contain the text 123</li>
     * </ul>
     * @param rowNum String row of the cell to check.
     * @param columnName String column of the cell to check.
     * @param tableName String name of the table.
     * @param assertion String if null is passed, looks for match(es), if any strong value is passed, looks for the value to not exist.
     * @param matchType String whether we are doing an exact match or a partial match.
     * @param textToMatch String the text to look for in the column.
     */
    @Then("^I wait for the cell in the (la|\\d+)(?:st|nd|rd|th) row and the (.*) column of the (.*?) to( not)? (has|have|contains?) the text (.*?)$")
    public static void waitForSpecificCellToHaveText(String rowNum, String columnName, String tableName, String assertion, String matchType, String textToMatch){
        boolean negate = !StringUtils.isEmpty(assertion);
        Table table = getElementAsTable(tableName);
        int rowIndex = rowNum.equals("la") ? table.getNumberOfRows() : Integer.parseInt(rowNum);
        boolean partialMatch = matchType.contains(CONTAIN);

        var expectedResult = SentinelStringUtils.format(
                "Expected the cell in the {} row and the {} column of the {} to {}contain the text {}.",
                SentinelStringUtils.ordinal(rowIndex), columnName, tableName, (negate ? "not " : ""), textToMatch);
        log.trace(expectedResult);
        Assert.assertTrue(expectedResult, table.waitForSpecificCellToContain((int)Time.longProcessTimeout().toSeconds(), columnName, rowIndex, textToMatch, partialMatch, negate));
    }
}