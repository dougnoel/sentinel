package com.dougnoel.sentinel.enums;

/**
 * A list of ways to identify table elements on a web page. Stored in an enum to prevent user error in calling methods.
 * <p>
 * <b>Valid Table Options:</b>
 * <ul>
 * <li>HTML - Standard HTML Table</li>
 * <li>NGXDATATABLE - ngx-datatable</li>
 * </ul>
 */
public enum TableType {
	HTML,
	NGXDATATABLE;
}
