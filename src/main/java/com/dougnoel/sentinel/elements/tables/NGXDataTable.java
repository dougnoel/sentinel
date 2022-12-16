package com.dougnoel.sentinel.elements.tables;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.dougnoel.sentinel.configurations.Time;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

/**
 * Implements an ngx-datatable WebElement. Contains functionality for counting values, finding values inside a table, and other
 * management issues, such as finding a value in the same row.
 */

public class NGXDataTable extends Table {
	private static final Logger log = LogManager.getLogger(NGXDataTable.class.getName()); // Create a logger.

	/**
	 * Creates an NGX-DataTable object overriding the Table creator and setting the values necessary for this object to work correctly.
	 * 
	 * @param elementName String the name of the element
	 * @param selectors Map&lt;String,String&gt; the list of selectors to use to find the element
	 */
	public NGXDataTable(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
		tableHeaderTag = "datatable-header-cell";
		tableRowTag = "datatable-body-row";
		tableCellDataTag = "datatable-body-cell";
		tableDataCellLocator = "//span";
		tableRowLocator = ".//" + tableRowTag;
		tableSiblingCellLocator = "//../../..//*";
		tableHeaderSortElementLocator = ".//span[contains(concat(' ', @class, ' '), ' sort-btn ')]";
	}
	protected By tableHeaderSelector = By.xpath(".//*[contains(concat(' ', @class, ' '), ' datatable-header-cell ')]");
	/**
	 * Returns number of row elements from getOrCreateRowElements
	 * 
	 * @see com.dougnoel.sentinel.elements.tables.Table#getOrCreateRowElements()
	 * @return int the number of row elements
	 */
	@Override
	public int getNumberOfRows() {
		final int numberOfRows = getOrCreateRowElements().size();
		log.trace("Number of rows found: {}", numberOfRows);
		return numberOfRows;
	}
	/**
	 * Returns all header elements. Internal method, intended to be called by getOrCreateHeaderElements().
	 *
	 * @return List&lt;WebElement&gt; the headers; or null if the header tags are not found
	 */
	@Override
	protected List<WebElement> getHeaderElements() {
		headerElements = this.element().findElements(tableHeaderSelector);
		return headerElements;
	}
	/**
	 * Returns a list of all the cell values in the given column.
	 * @param columnHeader String the column to search for all cell data
	 * @return ArrayList&lt;String&gt; a list of all cell data. Each entry in the
	 *         list corresponds to a cell.
	 */
	@Override
	public List<String> getAllCellDataForColumn(String columnHeader) {
		int arrayIndexOfHeader = getOrCreateHeaders().indexOf(columnHeader);
		if (arrayIndexOfHeader == -1) {
			String errorMessage = SentinelStringUtils.format("{} column does not exist.", columnHeader);
			log.error(errorMessage);
			throw new NoSuchElementException(errorMessage);
		}
		// add 1 because the List.getIndex method is 0-indexed and XPath is 1-indexed
		int xpathIndexOfHeader = arrayIndexOfHeader + 1;
		if (xpathIndexOfHeader == 6) {
			 xpathIndexOfHeader = arrayIndexOfHeader - 1;
		}


		List<String> cellData = new ArrayList<>();
		this.element().findElements(By.xpath(".//" + tableRowTag + "//" + tableCellDataTag + "[" + xpathIndexOfHeader + "]"))
				.forEach(cellElement -> cellData.add(fetchDataFromCellInterior(cellElement)));
		return cellData;
	}

	/**
	 * Accepts a WebElement for a cell, and attempts first to get a value from a child input.
	 * Failing this, it returns the text of the given element for items such as headers.
	 * @param cellElement WebElement the element object for the cell to fetch data from
	 * @return String the text/value found within the element
	 */
	private String fetchDataFromCellInterior(WebElement cellElement){
		String data;
		data = cellElement.getText();
		if (StringUtils.isBlank(data)){
			data = cellElement.findElement(By.tagName("input")).getAttribute("value");
		}
		return data;
	}
	@Override
	protected void createRowData() {
		long searchTime = Time.out().getSeconds() * 1000;
		long startTime = System.currentTimeMillis(); //fetch starting time
		while ((System.currentTimeMillis() - startTime) < searchTime) {
			try {
				var dataRows = getOrCreateRowElements();
				for (WebElement row : dataRows) {
					List<WebElement> cellElements = row.findElements(By.tagName(tableCellDataTag));
					ArrayList<String> cells = new ArrayList<>();
					cellElements.forEach(cellElement -> cells.add(fetchDataFromCellInterior(cellElement)));
					rows.add(cells);
				}
				return;
			} catch (org.openqa.selenium.StaleElementReferenceException sere) {
				rowElements = null; // reset the row elements so the ones that are stale aren't used in the next iteration
			}
		}
	}
}
