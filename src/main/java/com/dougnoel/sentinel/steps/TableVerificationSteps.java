package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElementAsTable;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import io.cucumber.java.en.Then;

public class TableVerificationSteps {
	private static final Logger log = LogManager.getLogger(TableVerificationSteps.class.getName()); // Create a logger.
	
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
     */
    @Then("^I see (\\d+) rows? in the (.*)$")
    public static void verifyNumberOfTableRows(int expectedNumberOfRows, String elementName) {
        int numberOfRows = getElementAsTable(elementName).getNumberOfRows();
        var expectedResult = SentinelStringUtils.format("Expected {} rows, found {} rows.", expectedNumberOfRows, numberOfRows);
        assertTrue(expectedResult, numberOfRows == expectedNumberOfRows);
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
    public static void compareTables(int pageNumber, String tableName) {
    	assertTrue(getElementAsTable(tableName).compareWithStoredTable(pageNumber));
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
	public static void verifyUniqueColumnText(String columnName, String isMultiCells, String tableName) {
		if (isMultiCells != null) {		
			assertTrue(getElementAsTable(tableName).verifyRowCellsAreUnique(columnName));
		} else {
			assertTrue(getElementAsTable(tableName).verifyColumnCellsAreUnique(columnName));
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
     */
    @Then("^I verify the (.*?) contains (?:a|the) (.*?) column$")
    public static void verifyColumnExists(String tableName, String columnName) {
        assertTrue(getElementAsTable(tableName).verifyColumnExists(columnName));
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
    public static void verifyStoredTextAppearsInColumn(String columnName, String tableName, String key) {
    	var textToMatch = Configuration.toString(key);
        assertTrue(getElementAsTable(tableName).verifyAnyColumnCellContains(columnName, textToMatch));
    }
    
    /**
     * Verifies a table column does or does not have the indicated text. It can check that any cell in the
     * column matches, or that all (or none) of the cells in the column match.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the Name column in the user info table contains the text Bill</li>
     * <li>I verify the State column in the Provider Info Table does not contain the text North Carolina</li>
     * <li>I verify all the cells in the Zip Code column in the Airports table contain the text 10001</li>
     * <li>I verify all the cells in the Store column in the Coffee Shop Table do not contain the text Roast</li>
     * </ul>
     * @param allCells String all cells must match if any value is passed, if null is passed on ly one cell must match
     * @param columnName String Name of the column to verify
     * @param tableName String Name of the table containing the column
     * @param assertion String if null is passed, looks for match(es), if any strong value is passed, looks for the value to not exist.
     * @param textToMatch String the text to look for in the column
     */
    @Then("^I verify( all the cells in)? the (.*?) column in the (.*?)( do(?:es)? not)? contains? the text (.*?)$")
    public static void verifyTextAppearsInColumn(String allCells, String columnName, String tableName, String assertion, String textToMatch) {
        boolean negate = !StringUtils.isEmpty(assertion);
        boolean orMatch = StringUtils.isEmpty(allCells);
        
        var expectedResult = SentinelStringUtils.format(
                "Expected the {} column of the {} to {}{} with the text {}. The element contained the text: {}",
                columnName, tableName, (negate ? "not " : ""), (orMatch ? "contain at least one cell" : "only contain cells"), textToMatch);
        log.trace(expectedResult);
        if (orMatch) {
            if (negate) {
                assertFalse(expectedResult, getElementAsTable(tableName).verifyAnyColumnCellContains(columnName, textToMatch));
            } else {
                assertTrue(expectedResult, getElementAsTable(tableName).verifyAnyColumnCellContains(columnName, textToMatch));
            }
        } else {
            if (negate) {
                assertFalse(expectedResult, getElementAsTable(tableName).verifyAllColumnCellsContain(columnName, textToMatch));
            } else {
                assertTrue(expectedResult, getElementAsTable(tableName).verifyAllColumnCellsContain(columnName, textToMatch));
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
    public static void verifyColumnSort(String columnName, String tableName, String sortOrder) {
        boolean sortAscending = StringUtils.equals(sortOrder, "ascending");
        
        var expectedResult = SentinelStringUtils.format("Expected the {} column of the {} to be sorted in {} order.", columnName, tableName, (sortAscending ? "ascending" : "descending"));
        log.trace(expectedResult);
        if (sortAscending) {
        	assertTrue(expectedResult, getElementAsTable(tableName).verifyColumnCellsAreSortedAscending(columnName));
        } else {
            assertTrue(expectedResult, getElementAsTable(tableName).verifyColumnCellsAreSortedDescending(columnName));
        }
    }
    
    /**
     * Verifies that a specific cell, given by the row and column, in the table contains the given text.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the cell in the last row and the Employee First Name column of the Employee table does not contain the text Alice</li>
     * <li>I verify the cell in the 2nd row and the Employee First Name column of the Employee table contains the text Bob</li>
     * </ul>
     * @param rowNum String the row number. Can be "la" to specify the last row, or an integer.
     * @param columnName String the name of the column to verify
     * @param tableName String the name of the table containing the column
     * @param assertion String if null is passed, looks for match(es), if any strong value is passed, looks for the value to not exist.
     * @param textToMatch String the text to look for in the column
     */
    @Then("^I verify the cell in the (la|\\d+)(?:st|nd|rd|th) row and the (.*) column of the (.*?)( do(?:es)? not)? contains? the text (.*?)$")
    public static void verifyCellInSpecifiedRow(String rowNum, String columnName, String tableName, String assertion, String textToMatch) {
    	boolean negate = !StringUtils.isEmpty(assertion);
    	
    	var table = getElementAsTable(tableName);
    	int rowIndex = rowNum.equals("la") ? table.getNumberOfRows() : Integer.parseInt(rowNum);
    	String actualText = table.getAllCellDataForColumn(columnName).get(rowIndex - 1);
    	
    	var expectedResult = SentinelStringUtils.format(
                "Expected the cell in the {} row and the {} column of the {} to {}contain the text {}. The element contained the text: {}",
                rowIndex, columnName, tableName, (negate ? "not " : ""), textToMatch, actualText);
    	log.trace(expectedResult);
    	if (negate) {
            assertFalse(expectedResult, table.verifySpecificCellContains(columnName, rowIndex, textToMatch));
        } else {
            assertTrue(expectedResult, table.verifySpecificCellContains(columnName, rowIndex, textToMatch));
        }
    }

}
