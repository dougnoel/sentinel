package com.dougnoel.sentinel.elements.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.dougnoel.sentinel.elements.PageElement;
import com.dougnoel.sentinel.enums.TableType;
import com.dougnoel.sentinel.exceptions.ElementNotFoundException;
import com.dougnoel.sentinel.exceptions.NoSuchColumnException;
import com.dougnoel.sentinel.strings.AlphanumComparator;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

/**
 * Implements a Table WebElement. contains functionality for counting values, finding values inside a table, and other
 * management issues, such as finding a value in the same row.
 */

public class Table extends PageElement {
	private static final Logger log = LogManager.getLogger(Table.class.getName()); // Create a logger.

	protected List<WebElement> headerElements = null; // Table Columns headers using <th> tags
	protected List<String> headers = new ArrayList<>(); // Column headers as text
	protected List<WebElement> rowElements = null; // Table Rows using <tr> tags
	protected List<ArrayList<String>> rows = new ArrayList<>(); // All text values of every row
	protected Map<String, ArrayList<String>> columns = new HashMap<>(); // All text values of every column
	protected Map<Integer, List<ArrayList<String>>> tables = new HashMap<>(); // Way to hold values of the same table on multiple pages.
	
	protected TableType tableType = TableType.HTML;
	protected String tableHeaderTag = "th";
	protected String tableRowTag = "tr";
	protected String tableCellDataTag = "td";
	protected String tableDataCellLocator = "//" + tableCellDataTag;
	protected String tableSiblingCellLocator = "//..//*";
	
	/**
	 * Creates a table object to manipulate. When used the table object finds and creates rows and columns and stores them. 
	 * If data in the table changes you must use the reset() method. You can save the current state of a table by calling 
	 * the storeTable() method.
	 *
	 * @param elementName String the name of the element
	 * @param selectors Map&lt;String,String&gt; the list of selectors to use to find the element
	 */
	public Table(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
	}

	/**
	 * Resets table data when comparing multiple pages of the same table.
	 */
	protected void reset() {
		if (headerElements != null) {
			headerElements.clear();
		}
		if (headers != null) {
			headers.clear();
		}
		if (rowElements != null) {
			rowElements.clear();
		}
		if (rows != null) {
			rows.clear();
		}
		if (columns != null) {
			columns.clear();
		}
	}

	/**
	 * Returns the headers in the table as a list of Strings, populates with the
	 * first row if there are no &lt;th&gt; tags. Creates headers if they do not already exist.
	 * 
	 * @return List&lt;String&gt; the headers of the table, populates with the first
	 *         row if there are no &lt;th&gt; tags
	 * @throws ElementNotFoundException if there is a problem finding the header or rows
	 */
	protected List<String> getOrCreateHeaders() {
		if (headers.isEmpty()) {
			getOrCreateHeaderElements();
			for (WebElement header : headerElements) {
				String headerText = header.getText();
				headers.add(headerText);
			}
		}
		// If we cannot find headers, then we need to populate this array with the first row
		if (headers.isEmpty()) {
			getOrCreateRows();
			List<String> firstRow = rows.get(0);
			for (String cell : firstRow) {
				headers.add(cell);
			}
		}
		log.trace("Headers: {}", headers);
		return headers;
	}

	/** 
	 *  Returns the header elements in the table as a list of WebElements, 
	 *  creates row elements if no &lt;th$gt; elements are found,
	 *  and logs the number of Header Elements
	 * 
	 * @return List&lt;WebElement&gt; the headers
	 * @throws ElementNotFoundException if the header elements cannot be found
	 */
	protected List<WebElement> getOrCreateHeaderElements() {
		if (headerElements == null) {
			headerElements = getHeaderElements();
		}
		if (headerElements == null) {
			log.trace("Header tags not found, using first row for headers.");
			headerElements = getOrCreateRowElements().get(0).findElements(By.tagName(tableCellDataTag));
		} else {
			log.trace("Header tags found");
		}
		log.trace("Number of Header Elements: {}", headerElements.size());
		return headerElements;
	}

	/**
	 * Returns all header elements. Internal method, intended to be called by getOrCreateHeaderElements().
	 * 
	 * @return List&lt;WebElement&gt; the headers; or null if the header tags are not found
	 * @throws ElementNotFoundException if the request is malformed
	 */
	protected List<WebElement> getHeaderElements() {
		headerElements = this.element().findElements(By.tagName(tableHeaderTag));
		return headerElements;
	}
	
	/**
	 * Returns true if the table has &lt;th&gt; elements, otherwise returns false
	 * even though the first row will be used to populate the headers list.
	 * 
	 * @see com.dougnoel.sentinel.elements.tables.Table#getOrCreateHeaderElements()
	 * @return boolean true if the table has &lt;th&gt; elements, otherwise false
	 * @throws ElementNotFoundException if an element is not found
	 */
	protected boolean tableHeadersExist()  {
		return getHeaderElements() != null;
	}

	/**
	 * Returns &lt;tr&gt; elements found in a table. 
	 * 
	 * @return List&lt;WebElement&gt;
	 * @throws ElementNotFoundException if the row elements cannot be found
	 */
	protected List<WebElement> getOrCreateRowElements() {
		if (rowElements == null) {
			rowElements = this.element().findElements(By.tagName(tableRowTag));
		}
		return rowElements;
	}
	
	/**
	 * Returns array of cell arrays, with data for each cell, in the table. Initial row of table headers is removed
	 * 
	 * @return List&lt;ArrayList&lt;String&gt;&gt;
	 * @throws ElementNotFoundException if the row elements cannot be found
	 */
	protected List<ArrayList<String>> getOrCreateRows() {
		if (rows.isEmpty()) {
			List<WebElement> dataRows = getOrCreateRowElements();
				dataRows.remove(0);
			for (WebElement row : dataRows) {
				List<WebElement> cellElements = row.findElements(By.tagName(tableCellDataTag));
				ArrayList<String> cells = new ArrayList<>();
				for (WebElement cell : cellElements) {
					cells.add(cell.getText());
				}
				rows.add(cells);
			}
		}
		log.trace("Rows Data: {}", rows);
		return rows;
	}

	/**
	 * Returns number of row elements from getOrCreateRowElements
	 * 
	 * @see com.dougnoel.sentinel.elements.tables.Table#getOrCreateRowElements()
	 * @return int the number of row elements
	 * @throws ElementNotFoundException if an element is not found
	 */
	public int getNumberOfRows() {
		//Selenium counts a <th> tag as a <td> tag and returns it.
		if (tableHeadersExist() && tableType != TableType.HTML) {
			return getOrCreateRowElements().size();
		}
		return getOrCreateRowElements().size() - 1;
	}

	/**
	 * Returns the mapping of header strings to cell arrays for each column in the table. 
	 * e.g. { "Date Column": ["1/1/01", "1/2/01", ...] }
	 * 
	 * @return Map&lt;String, ArrayList&lt;String&gt;&gt;
	 * @throws ElementNotFoundException if an element is not found
	 */
	protected Map<String, ArrayList<String>> getOrCreateColumns() {
		if (columns.isEmpty()) {
			int index = 0;
			getOrCreateRows(); // We cannot create the columns without Row data
			for (String header : getOrCreateHeaders()) {
				ArrayList<String> cells = new ArrayList<>();
				for (ArrayList<String> row : rows) {
					cells.add(row.get(index));
				}
				columns.put(header, cells);
				index++;
			}
		}
		log.trace("Columns Data: {}", columns);
		return columns;
	}
	
	/**
	 * Returns the number of columns in the table.
	 * 
	 * @see com.dougnoel.sentinel.elements.tables.Table#getOrCreateHeaders()
	 * @return int the number of columns
	 * @throws ElementNotFoundException if an element is not found
	 */
	public int getNumberOfColumns() {
		return getOrCreateHeaders().size();
	}

	/**
	 * Returns list of all cells in the given tableRow, and logs the first cell and then row cells in separate log entries.
	 * 
	 * @param tableRow WebElement a row in the table
	 * @return List&lt;WebElement;&gt; 
	 */
	protected List<WebElement> getCells(WebElement tableRow) {
		List<WebElement> cells = tableRow.findElements(By.tagName(tableCellDataTag));
		log.debug("First Cell: {}", tableRow.findElement(By.tagName(tableCellDataTag)));
		log.debug("Row Cells: {}", cells);
		return cells;
	}

	/**
	 * Stores the current table rows on the current page in an index given by the
	 * integer passed in the pageNumber parameter. Used for comparing paginated
	 * table data.
	 * 
	 * @param pageNumber int the page number under which to store the table data for comparison
	 * @throws ElementNotFoundException if an element is not found
	 */
	public void storeTable(int pageNumber) {
		reset();
		tables.put(pageNumber, getOrCreateRows());
	}

	/**
	 * Stores the current tables rows in index 1. Used for single-page
	 * (un-paginated) tables.
	 * @throws ElementNotFoundException if an element is not found
	 */
	public void storeTable() {
		storeTable(1);
	}

	/**
	 * Compares the given number of a page stored in memory and compares it to the one
	 * currently being displayed. If all the rows match, returns true. If any data
	 * is different, returns false.
	 * 
	 * @param pageNumber int the stored page number to compare against the current page
	 * @return boolean Table matches the one in memory.
	 * @throws ElementNotFoundException if an element is not found
	 */
	public boolean compareWithStoredTable(int pageNumber) {
		reset();
		return (tables.get(pageNumber) == getOrCreateRows());
	}

	/**
	 * Returns a WebElement found inside the indicated row, taking unique text to find the row and unique 
	 * text to find a specific element in that row.
	 * 
	 * @param elementLocatorText String the text of the element (link) you are looking to find
	 * @param rowLocatorText String the unique text to locate the row to search
	 * @return org.openqa.selenium.WebElement the first element inside the table that was found using the given locator
	 * @throws ElementNotFoundException if an element is not found
	 */
	public WebElement getElementInRowThatContains(String rowLocatorText, String elementLocatorText) {
		return getElementInRowThatContains(By.xpath("[contains(text(),'" + rowLocatorText + "')]"), By.xpath("[contains(text(),'" + elementLocatorText + "')]"));
	}
	
	/**
	 * Returns a WebElement found inside the indicated row using the locator passed.
	 * 
	 * @param ordinalRow int takes -1 , 1...n where -1 signifies the last row
	 * @param elementLocator org.openqa.selenium.By the locator to use to find the element
	 * @return org.openqa.selenium.WebElement the first element inside the table that was found using the given locator
	 * @throws ElementNotFoundException if no element is found
	 */
	public WebElement getElementInRowThatContains(int ordinalRow, By elementLocator) {
		WebElement element;
		if (ordinalRow == -1) {
			//Set to the last row
			ordinalRow = getNumberOfRows()-1;
		}
		
		try {
			element = getOrCreateRowElements().get(ordinalRow--)
					.findElement(By.xpath(tableDataCellLocator))
					.findElement(elementLocator);
		} catch (org.openqa.selenium.NoSuchElementException e) {
			String errorMsg = SentinelStringUtils.format("{} not found in row {} Error: {}", elementLocator, ordinalRow, e.getMessage());
			log.error(errorMsg);
			throw new com.dougnoel.sentinel.exceptions.NoSuchElementException(errorMsg);
		}
		log.trace("Element found: {}", element);
		return element;
	}
	
	/**
	 * Returns a WebElement found inside the indicated row, taking a locator to find the row and a locator 
	 * to find a specific element in that row.
	 * 
	 * @param rowLocator org.openqa.selenium.By the locator to use to find the row to search
	 * @param elementLocator org.openqa.selenium.By the locator to use to find the element
	 * @return org.openqa.selenium.WebElement the first element inside the table that was found using the given locator
	 * @throws ElementNotFoundException if no element is found
	 */
	public WebElement getElementInRowThatContains(By rowLocator, By elementLocator) {
		WebElement element;
		try {
			element = this.element()
					.findElement(By.xpath(tableDataCellLocator))
					.findElement(rowLocator)
					.findElement(By.xpath(tableSiblingCellLocator))
					.findElement(elementLocator);

		} catch (org.openqa.selenium.NoSuchElementException e) {
			String errorMsg = SentinelStringUtils.format("{} not found in the row with {} Error: {}", elementLocator, rowLocator, e.getMessage());
			log.error(errorMsg);
			throw new com.dougnoel.sentinel.exceptions.NoSuchElementException(errorMsg);
		}
		log.trace("Element found: {}", element);
		return element;
	}

	/**
	 * Clicks a link in a row by using text in another cell. For example,
	 * finding an "Edit" link for a specific username in a table. To work, this
	 * method must have a link name and a unique string to match. This method could
	 * also be used to validate text in a cell in the same row Returning
	 * successfully would indicate the expected text was found.
	 * 
	 * @param elementText String the text of the element (link) you are looking to find
	 * @param textToClick String the unique text to locate the row in question
	 * @throws ElementNotFoundException if an element is not found
	 */
	public void clickElementInRowThatContains(String elementText, String textToClick) {
		getElementInRowThatContains(elementText, textToClick).click();
	}

	/**
	 * Clicks on an element found by elementLocator, in a row found by rowLocator.
	 * 
	 * @param rowLocator org.openqa.selenium.By the locator to use to find the row to search
	 * @param elementLocator org.openqa.selenium.By the locator to use to find the element
	 * @throws ElementNotFoundException if an element is not found
	 */
	public void clickElementInRowThatContains(By rowLocator, By elementLocator) {
		getElementInRowThatContains(rowLocator, elementLocator).click();
	}
	
	/**
	 * Clicks on an element found by elementLocator, in a row found by ordinalRow.
	 * 
	 * @param ordinalRow int takes -1 , 1...n where -1 signifies the last row
	 * @param elementLocator org.openqa.selenium.By the locator to use to find the element
	 * @throws ElementNotFoundException if an element is not found
	 */
	public void clickElementInRowThatContains(int ordinalRow, By elementLocator) {
		getElementInRowThatContains(ordinalRow, elementLocator).click();
	}
	
	/**
	 * Returns true if all cells in the given column match the text value given.
	 * 
	 * @param columnHeader String the name of the column
	 * @param textToMatch String the text that should be in every cell
	 * @return boolean true if the column contains the given text in every cell, false if not
	 * @throws ElementNotFoundException if an element is not found
	 */
	public boolean verifyAllColumnCellsContain(String columnHeader, String textToMatch) {
		getOrCreateHeaders();
		ArrayList<String> column = getOrCreateColumns().get(columnHeader);
		if (column == null) {
			String errorMessage = SentinelStringUtils.format("{} column does not exist.", columnHeader);
			log.error(errorMessage);
			throw new ElementNotFoundException(errorMessage);
		}
		for (String cell : column) {
			try {
				if (!cell.contains(textToMatch)) {
					log.debug("Not all values in the {} column are equal to {}. Cell contained the data: {}. False result returned.", columnHeader, textToMatch, cell);
					return false;
				}
			} catch (NullPointerException e) {
				String errorMessage = SentinelStringUtils.format("NullPointerException triggered when searching for the value {} in the {} column. Value found: {}", textToMatch, columnHeader, cell);
				log.error(errorMessage);
				throw new ElementNotFoundException(errorMessage, e);
			}

		}
		return true;
	}

	/**
	 * Returns true if any cells in the given column match the text value given.
	 * 
	 * @param columnHeader String the name of the column
	 * @param textToMatch String the text that should be in at least one of the cells
	 * @return boolean true if the column contains the given text in at least one of the cells, false if not
	 * @throws ElementNotFoundException if an element is not found
	 */
	public boolean verifyAnyColumnCellContains(String columnHeader, String textToMatch) {
		getOrCreateHeaders();
		ArrayList<String> column = getOrCreateColumns().get(columnHeader);
		if (column == null) {
			String errorMessage = SentinelStringUtils.format("{} column does not exist.", columnHeader);
			log.error(errorMessage);
			throw new ElementNotFoundException(errorMessage);
		}
		for (String cell : column) {
			try {
				if (cell.contains(textToMatch)) {
					return true;
				}
				else {
					log.trace("Looking for {} in the {} column. Found: {}", textToMatch, columnHeader, cell);
				}
			} catch (NullPointerException e) {
				String errorMessage = SentinelStringUtils.format("NullPointerException triggered when searching for the value {} in the {} column. Value found: {}", textToMatch, columnHeader, cell);
				log.error(errorMessage);
				throw new ElementNotFoundException(errorMessage, e);
			}

		}
		log.debug("No values in the {} column are equal to {}. False result returned. Turn on trace logging level to see all values found.", columnHeader, textToMatch);
		return false;
	}
	
	/**
	 * Checks all the cells in a given column and verifies they are sorted in ascending order.
	 * 
	 * @param columnName String the name of the column you want to evaluate
	 * @return boolean true is the column is sorted in ascending order, false if it is not sorted correctly
	 * @throws ElementNotFoundException if an element is not found
	 */
	public boolean verifyColumnCellsAreSortedAscending(String columnName) {
		return verifyColumnCellsAreSorted(columnName, true);
	}
	
	/**
	 * Checks all the cells in a given column and verifies they are sorted in ascending order.
	 * 
	 * @param columnName String the name of the column you want to evaluate
	 * @return boolean true is the column is sorted in descending order, false if it is not sorted correctly
	 * @throws ElementNotFoundException if an element is not found
	 */
	public boolean verifyColumnCellsAreSortedDescending(String columnName) {
		return verifyColumnCellsAreSorted(columnName, false);
	}
	
	/**
	 * Checks all the cells in a given column and verifies they are sorted in requested Comparator order.
	 * <p>
	 * NOTE: Use the verifyColumnCellsAreSortedAscending() and verifyColumnCellsAreSortedDescending() methods
	 * unless you have a custom sort to pass. Passing the wrong value can cause errors.
	 * 
	 * @param columnName String the name of the column you want to evaluate
	 * @param sortOrderAscending boolean true for ascending, false for descending
	 * @return boolean true is the column is sorted in the passed sort order, false if it is not sorted correctly
	 * @throws ElementNotFoundException if an element is not found
	 */
	public boolean verifyColumnCellsAreSorted(String columnName, boolean sortOrderAscending) {
		getOrCreateHeaders();
		ArrayList<String> column = getOrCreateColumns().get(columnName);
		@SuppressWarnings("unchecked")
		ArrayList<String> sortedColumn = (ArrayList<String>) column.clone();
		
		// We needs to sort the strings taking into account there might be numbers in the strings.
		// We do that with a special sort.
		Collections.sort(sortedColumn, new AlphanumComparator());
		//If we want a descending sort order, we need to use the custom sort above, then reverse it.
		if (!sortOrderAscending)
		{
			Collections.reverse(sortedColumn);	
		}
		log.trace("Sort Order: {}\n"
				+ "Original Column Data: {}\n"
				+ "  Sorted Column Data: {}", 
				sortOrderAscending ? "Ascending" : "Descending", column, sortedColumn);

		return column.equals(sortedColumn);
	}
	
	/**
	 * Returns &lt;code&gt;true&lt;/code&gt; if the column cells are unique
	 * 
	 * @param columnHeader String text of the given column header to search
	 * @return boolean true if column cells are unique, false if duplicates are found, throws error otherwise
	 * @throws ElementNotFoundException if an element is not found
	 */
	public boolean verifyColumnCellsAreUnique(String columnHeader) {
		if (!verifyColumnExists(columnHeader)) {
			log.error("IllegalArgumentException: Column header \"{}\" does not exist.", columnHeader);
			throw new IllegalArgumentException("Column header \"" + columnHeader + "\" does not exist.");
		}
		getOrCreateHeaders();
		ArrayList<String> column = getOrCreateColumns().get(columnHeader);
		if (column.isEmpty()) {
			log.error("Header text: {}", columnHeader);
			throw new IllegalArgumentException("Column header \"" + columnHeader + "\" does not exist.");
		}
		for (String cell : column) {
			try {
				int count = 0;
				for (String cell2 : column) {
					if (cell2.contains(cell)) {
						count++;
					}
					if (count > 1) {
						log.error("False result returned. Header text: {} | Cell data: {}", columnHeader, cell);
						return false;
					}
				}

			} catch (NullPointerException e) {
				log.error("NullPointerException thrown. Header text: {} | Cell data: {}", columnHeader, cell);
				throw e;
			}

		}
		return true;
	}

	/**
	 * Returns true if column exists, false if column does not exist
	 * 
	 * @param columnName String name of column to find
	 * @return boolean true if column exists, false if column does not exists.
	 * @throws ElementNotFoundException if an element is not found
	 */
	public boolean verifyColumnExists(String columnName) {
		for (String header : getOrCreateHeaders()) {
			if (header.contains(columnName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the row cell's values are unique for the given column name
	 * 
	 * @param columnName String comma delimited columns list
	 * @return boolean true if all cells values are unique, false if any duplicates
	 * @throws ElementNotFoundException if an element is not found
	 */
	public boolean verifyRowCellsAreUnique(String columnName) {
		String[] columnHeaders = columnName.split(", ");
		return verifyRowCellsAreUnique(columnHeaders);
	}

	/**
	 * Returns true if the cell values are unique for the given array of column names
	 * 
	 * @param columnHeaders string[] the array of column name to validate
	 * @return boolean true if all cells values are unique, false if any duplicates
	 * @throws ElementNotFoundException if an element is not found
	 */
	public boolean verifyRowCellsAreUnique(String[] columnHeaders) {

		getOrCreateHeaders();
		getOrCreateRows();
		List<Integer> indexes = new ArrayList<>();
		for (String columnHeader : columnHeaders) {
			if (!verifyColumnExists(columnHeader)) {
				String errorMessage = SentinelStringUtils.format("Column header \"{}\" does not exist.", columnHeader);
				log.error(errorMessage);
				throw new NoSuchColumnException(errorMessage);
			}
			for (int i = 0; i < headers.size(); i++) {

				if (columnHeader.equals(headers.get(i)))
					indexes.add(i);
			}
		}
		for (List<String> cells : rows) {

			String cellValue = getCellsValue(indexes, cells);

			int count = 0;
			for (List<String> cells2 : rows) {
				String cellValue2 = getCellsValue(indexes, cells2);
				if (cellValue2.contains(cellValue)) {
					count++;
				}
				if (count > 1) {
					log.trace("False result returned. Header text: {} | Cell data: {}", indexes, cellValue);
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Returns all the cells values in the listed columns
	 * 
	 * @param indexes List&lt;Integer&gt; listed column index
	 * @param cells List&lt;String&gt; row cells
	 * @return String of listed columns row values
	 */
	private String getCellsValue(List<Integer> indexes, List<String> cells) {
		String cellValues = "";
		for (int index = 0; index < indexes.size(); index++) {
			cellValues = cellValues.concat(cells.get(indexes.get(index)));
		}
		return cellValues;
	}

}
