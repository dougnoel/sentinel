package com.dougnoel.sentinel.elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.enums.TableType;
import com.dougnoel.sentinel.exceptions.ElementNotFoundException;
import com.dougnoel.sentinel.exceptions.NoSuchColumnException;
import com.dougnoel.sentinel.strings.StringUtils;

/**
 * Implements a Table WebElement. contains functionality for counting values, finding values inside a table, and other
 * management issues, such as finding a value in the same row.
 */

public class Table extends PageElement {
	private static final Logger log = LogManager.getLogger(Table.class.getName()); // Create a logger.

	private TableType tableType = TableType.HTML;
	private String tableHeaderTag = "th";
	private String tableRowTag = "tr";
	private String tableCellDataTag = "td";
	
	protected List<WebElement> headerElements = null; // Table Columns headers using <th> tags
	protected List<String> headers = new ArrayList<String>(); // Column headers as text
	protected List<WebElement> rowElements = null; // Table Rows using <tr> tags
	protected List<ArrayList<String>> rows = new ArrayList<ArrayList<String>>(); // All text values of every row
	protected Map<String, ArrayList<String>> columns = new HashMap<>(); // All text values of every column
	protected Map<Integer, List<ArrayList<String>>> tables = new HashMap<>(); // Way to hold values of the same table on multiple pages.

	/**
	 * Creates a table object to manipulate. Expects a table or ngx-datatable tag. When used
	 * the table object finds and creates rows and columns and stores them. If data in the table changes
	 * you must use the reset() method. You can save the current state of a table by calling the storeTable()
	 * method.
	 * NOTE: Unlike other element methods, this one throws an ElementNotFound exception because the constructor
	 * must look at the tag to determine the table type.
	 * TODO: To make this more easily extensible, this should be a factory method, creating and returning
	 * table objects based on the tag name.
	 * 
	 * @param selectorType SelectorType the type of selector to use
	 * @param selectorValue String the value to look for with the given selector type
	 * TODO: @throws ElementNotFoundException if the element cannot be found, not changing this now to make Page Objects backwards compatible. Should handle this when we change page objects to yml completely.
	 */
	public Table(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
		
		try {
			if (this.toWebElement().getTagName().contains("ngx-datatable")) {
				tableType = TableType.NGXDATATABLE;
				tableHeaderTag = "datatable-header-cell";
				tableRowTag = "datatable-body-row";
				tableCellDataTag = "datatable-body-cell";
			}
		} catch (ElementNotFoundException e) {
			log.error(e.getStackTrace()); //Suppress this for now.
		}
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
	protected List<String> getOrCreateHeaders() throws ElementNotFoundException {
		if (headers.isEmpty()) {
			getOrCreateHeaderElements();
			for (WebElement header : headerElements) {
				String headerText = header.getText();
				log.trace("Header Text: {}", headerText);
				headers.add(headerText);
			}
		}
		// If we cannot find headers, then we need to populate this array with the first
		// row
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
	 * @return List&lt;WebElement&gt;
	 * @throws ElementNotFoundException if the header elements cannot be found
	 */
	protected List<WebElement> getOrCreateHeaderElements() throws ElementNotFoundException {
		if (headerElements == null) {
			headerElements = this.element().findElements(By.tagName(tableHeaderTag));
		}
		if (headerElements == null) {
			headerElements = getOrCreateRowElements().get(0).findElements(By.tagName(tableCellDataTag));
		}
		log.trace("Number of Header Elements: {}", headerElements.size());
		return headerElements;
	}

	/**
	 * Returns true if the table has &lt;th&gt; elements, otherwise returns false
	 * even though the first row will be used to populate the headers list.
	 * 
	 * @see com.dougnoel.sentinel.elements.Table#getOrCreateHeaderElements()
	 * @return boolean true if the table has &lt;th&gt; elements, otherwise false
	 * @throws ElementNotFoundException if an element is not found
	 */
	protected boolean tableHeadersExist() throws ElementNotFoundException  {
		return getOrCreateHeaderElements() != null;
	}

	/**
	 * Returns &lt;tr&gt; elements found in a table. 
	 * 
	 * @return List&lt;WebElement&gt;
	 * @throws ElementNotFoundException if the row elements cannot be found
	 */
	protected List<WebElement> getOrCreateRowElements() throws ElementNotFoundException  {
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
	protected List<ArrayList<String>> getOrCreateRows() throws ElementNotFoundException {
		if (rows.isEmpty()) {
			List<WebElement> dataRows = getOrCreateRowElements();
			dataRows.remove(0); // Header row
			for (WebElement row : dataRows) {
				List<WebElement> cellElements = row.findElements(By.tagName(tableCellDataTag));
				ArrayList<String> cells = new ArrayList<String>();
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
	 * @see com.dougnoel.sentinel.elements.Table#getOrCreateRowElements()
	 * @return int the number of row elements
	 * @throws ElementNotFoundException if an element is not found
	 */
	public int getNumberOfRows() throws ElementNotFoundException  {
		return getOrCreateRowElements().size() - 1;
	}

	/**
	 * Returns the mapping of header strings to cell arrays for each column in the table. 
	 * e.g. { "Date Column": ["1/1/01", "1/2/01", ...] }
	 * 
	 * @return Map&lt;String, ArrayList&lt;String&gt;&gt;
	 * @throws ElementNotFoundException if an element is not found
	 */
	protected Map<String, ArrayList<String>> getOrCreateColumns() throws ElementNotFoundException {
		if (columns.isEmpty()) {
			int index = 0;
			getOrCreateRows(); // We cannot create the columns without Row data
			for (String header : getOrCreateHeaders()) {
				ArrayList<String> cells = new ArrayList<String>();
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
	 * @see com.dougnoel.sentinel.elements.Table#getOrCreateHeaders()
	 * @return int the number of columns
	 * @throws ElementNotFoundException if an element is not found
	 */
	public int getNumberOfColumns() throws ElementNotFoundException {
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
	public void storeTable(int pageNumber) throws ElementNotFoundException {
		reset();
		tables.put(pageNumber, getOrCreateRows());
	}

	/**
	 * Stores the current tables rows in index 1. Used for single-page
	 * (un-paginated) tables.
	 * @throws ElementNotFoundException if an element is not found
	 */
	public void storeTable() throws ElementNotFoundException  {
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
	public boolean compareWithStoredTable(int pageNumber) throws ElementNotFoundException {
		reset();
		return (tables.get(pageNumber) == getOrCreateRows());
	}

	//TODO: Update for ngx-table
	/**
	 * Finds the link in a row by using text in another cell. For example,
	 * finding an "Edit" link for a specific username in a table. To work, this
	 * method must have a link name and a unique string to match. This method could
	 * also be used to validate text in a cell in the same row Returning
	 * successfully would indicate the expected text was found.
	 * 
	 * @param elementText String the text of the element (link) you are looking to find.
	 * @param textToMatch String the unique text to locate the row in question.
	 * @return WebElement a selenium WebElement object that can be operated on.
	 * @throws ElementNotFoundException if an element is not found
	 */
	protected WebElement getElementInRowThatContains(String elementText, String textToMatch) throws ElementNotFoundException {
		try {
			if (tableType == TableType.NGXDATATABLE) {
				return this.element().findElement(By.xpath(
						"//span[contains(text(),'" + elementText + 
						"')]/../../..//*[contains(text(),'" + textToMatch + "')]"));
			} else
			{
				return this.element().findElement(By.xpath(
						"//" + tableCellDataTag + "[contains(text(),'" + elementText + 
						"')]/..//*[contains(text(),'" + textToMatch + "')]"));
			}
		} catch (org.openqa.selenium.NoSuchElementException e) {
			String errorMsg = StringUtils.format("{} not found in {} Error: {}", textToMatch, elementText, e.getMessage());
			log.error(errorMsg);
			throw new com.dougnoel.sentinel.exceptions.NoSuchElementException(errorMsg);
		}
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
	public void clickElementInRowThatContains(String elementText, String textToClick) throws ElementNotFoundException {
		getElementInRowThatContains(elementText, textToClick).click();
	}

	/**
	 * Returns true if all cells in the given column match the text value given.
	 * 
	 * @param columnHeader String the name of the column
	 * @param textToMatch String the text that should be in every cell
	 * @return boolean true if the column contains the given text in every cell, false if not
	 * @throws ElementNotFoundException if an element is not found
	 */
	public boolean verifyAllColumnCellsContain(String columnHeader, String textToMatch) throws ElementNotFoundException  {
		getOrCreateHeaders();
		ArrayList<String> column = getOrCreateColumns().get(columnHeader);
		if (column == null) {
			String errorMessage = StringUtils.format("{} column does not exist.", columnHeader);
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
				String errorMessage = StringUtils.format("NullPointerException triggered when searching for the value {} in the {} column. Value found: {}", textToMatch, columnHeader, cell);
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
	public boolean verifyAnyColumnCellContains(String columnHeader, String textToMatch) throws ElementNotFoundException  {
		getOrCreateHeaders();
		ArrayList<String> column = getOrCreateColumns().get(columnHeader);
		if (column == null) {
			String errorMessage = StringUtils.format("{} column does not exist.", columnHeader);
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
				String errorMessage = StringUtils.format("NullPointerException triggered when searching for the value {} in the {} column. Value found: {}", textToMatch, columnHeader, cell);
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
	public boolean verifyColumnCellsAreSortedAscending(String columnName) throws ElementNotFoundException {
		return verifyColumnCellsAreSorted(columnName, null);
	}
	
	/**
	 * Checks all the cells in a given column and verifies they are sorted in ascending order.
	 * 
	 * @param columnName String the name of the column you want to evaluate
	 * @return boolean true is the column is sorted in descending order, false if it is not sorted correctly
	 * @throws ElementNotFoundException if an element is not found
	 */
	public boolean verifyColumnCellsAreSortedDescending(String columnName) throws ElementNotFoundException {
		return verifyColumnCellsAreSorted(columnName, Collections.reverseOrder());
	}
	
	/**
	 * Checks all the cells in a given column and verifies they are sorted in requested Comparator order.
	 * <p>
	 * NOTE: Use the verifyColumnCellsAreSortedAscending() and verifyColumnCellsAreSortedDescending() methods
	 * unless you have a custom sort to pass. Passing the wrong value can cause errors.
	 * 
	 * @param columnName String the name of the column you want to evaluate
	 * @param sortOrder Comparator the sort you want to do on the ArrayList&lt;String;&gt;, passing null will sort in ascending order
	 * @return boolean true is the column is sorted in the passed sort order, false if it is not sorted correctly
	 * @throws ElementNotFoundException if an element is not found
	 */
	@SuppressWarnings("unchecked")
	public boolean verifyColumnCellsAreSorted(String columnName, @SuppressWarnings("rawtypes") Comparator sortOrder) throws ElementNotFoundException {
		getOrCreateHeaders();
		ArrayList<String> column = getOrCreateColumns().get(columnName);
		ArrayList<String> sortedColumn = (ArrayList<String>) column.clone();
		if (sortOrder == null)
		{
			Collections.sort(sortedColumn);
		} else {
			Collections.sort(sortedColumn, sortOrder);	
		}
		if (column.equals(sortedColumn)) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns &lt;code&gt;true&lt;/code&gt; if the column cells are unique
	 * 
	 * @param columnHeader String text of the given column header to search
	 * @return boolean true if column cells are unique, false if duplicates are found, throws error otherwise
	 * @throws ElementNotFoundException if an element is not found
	 */
	public boolean verifyColumnCellsAreUnique(String columnHeader) throws ElementNotFoundException  {
		if (verifyColumnExists(columnHeader) == false) {
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
	public boolean verifyColumnExists(String columnName) throws ElementNotFoundException  {
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
	public boolean verifyRowCellsAreUnique(String columnName) throws ElementNotFoundException  {
		String[] columns = columnName.split(", ");
		return verifyRowCellsAreUnique(columns);
	}

	/**
	 * Returns true if the cell values are unique for the given array of column names
	 * 
	 * @param columnsHeader string[] the array of column name to validate
	 * @return boolean true if all cells values are unique, false if any duplicates
	 * @throws ElementNotFoundException if an element is not found
	 */
	public boolean verifyRowCellsAreUnique(String[] columnsHeader) throws ElementNotFoundException {

		getOrCreateHeaders();
		getOrCreateRows();
		List<Integer> indexes = new ArrayList<Integer>();
		for (String columnHeader : columnsHeader) {
			if (verifyColumnExists(columnHeader) == false) {
				String errorMessage = StringUtils.format("Column header \"{}\" does not exist.", columnHeader);
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
