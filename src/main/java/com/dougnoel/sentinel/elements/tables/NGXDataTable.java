package com.dougnoel.sentinel.elements.tables;

import java.util.Map;

import com.dougnoel.sentinel.enums.TableType;

/**
 * Implements an ngx-datatable WebElement. Contains functionality for counting values, finding values inside a table, and other
 * management issues, such as finding a value in the same row.
 */

public class NGXDataTable extends Table {

	/**
	 * Creates an NGX-DataTable object overriding the Table creator and setting the values necessary for this object to work correctly.
	 * 
	 * @param elementName String the name of the element
	 * @param selectors Map&lt;String,String&gt; the list of selectors to use to find the element
	 */
	public NGXDataTable(String elementName, Map<String,String> selectors) {
		super(elementName, selectors);
		tableType = TableType.NGXDATATABLE;
		tableHeaderTag = "datatable-header-cell";
		tableRowTag = "datatable-body-row";
		tableCellDataTag = "datatable-body-cell";
		tableDataCellLocator = "//span";
		tableSiblingCellLocator = "//../../..//*";
	}
	
}
