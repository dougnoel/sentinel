package com.dougnoel.sentinel.enums;

/**
 * A list of ways to identify web elements on a web page. Stored in an enum to prevent user error in calling methods.
 * Also stores ways to find a select option in a select element.
 * <p>
 * <b>Valid PageElement Options:</b>
 * <ul>
 * <li>CSS - WebDriver Find Element By CSS</li>
 * <li>ID - WebDriver Find Element By Id</li>
 * <li>NAME - WebDriver Find Element By Name</li>
 * <li>PARTIALTEXT - WebDriver Find Element By PartialLinkText</li>
 * <li>TEXT - WebDriver Find Element By LinkText</li>
 * <li>XPATH - WebDriver Find Element By XPath</li>
 * </ul>
 * <b>Valid PageSelectElement Options:</b>
 * <ul>
 * <li>INDEX - WebDriver Select By Index</li>
 * <li>TEXT - WebDriver Select By Text</li>
 * <li>VALUE - WebDriver Select By Value</li>
 * </ul>
 */
public enum SelectorType {
	CLASS,
	CSS,
	ID,
	INDEX, 	// Used for PageSelectElement Only
	NAME,
	PARTIALTEXT,
	TEXT, 	// Used for both PageElement and PageSelect Element
	VALUE, 	//Used for PageSelectElement Only
	XYZ,
	XPATH;
}
