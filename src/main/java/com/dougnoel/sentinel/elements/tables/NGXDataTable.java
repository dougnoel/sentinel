package com.dougnoel.sentinel.elements.tables;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
}
