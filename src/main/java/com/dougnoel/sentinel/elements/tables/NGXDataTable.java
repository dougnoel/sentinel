package com.dougnoel.sentinel.elements.tables;

import com.dougnoel.sentinel.enums.SelectorType;
import com.dougnoel.sentinel.enums.TableType;

/**
 * Implements an ngx-datatable WebElement. Contains functionality for counting values, finding values inside a table, and other
 * management issues, such as finding a value in the same row.
 */

public class NGXDataTable extends Table {

	
	/**
	 * Creates an NGX-DataTable object overriding the Table creator and setting the values necessary for this object to work correctly.
	 * 
	 * @param selectorType SelectorType the type of selector to use
	 * @param selectorValue String the value to look for with the given selector type
	 */
	public NGXDataTable(SelectorType selectorType, String selectorValue) {
		super(selectorType, selectorValue);
		tableType = TableType.NGXDATATABLE;
		tableHeaderTag = "datatable-header-cell";
		tableRowTag = "datatable-body-row";
		tableCellDataTag = "datatable-body-cell";
		tableDataCellLocator = "//span";
		tableSiblingCellLocator = "//../../..//*";
	}

}
