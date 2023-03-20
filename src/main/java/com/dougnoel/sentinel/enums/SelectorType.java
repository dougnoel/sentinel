package com.dougnoel.sentinel.enums;

/**
 * A list of ways to identify web elements on a web page. Stored in an enum to prevent user error in calling methods.
 * Also stores ways to find a select option in a select element.
 * <p>
 * <b>Valid Element Options:</b>
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
 * <b>Valid WindowsElement Options:</b>
 * <ul>
 * <li>ACCESSIBILITYID, AUTOMATIONID</li>
 * <li>CLASS, CLASSNAME</li>
 * <li>ID, RUNTIMEID</li>
 * <li>NAME</li>
 * <li>TAGNAME**, LOCALIZEDCONTROLTYPE**</li>
 * <li>XPATH</li>
 * </ul>
 */
public enum SelectorType {
	ACCESSIBILITYID, //Used for Windows Element Only
	AUTOMATIONID, //Used for Windows Element Only
	CLASS,
	CLASSNAME, //Used for Windows Element Only
	CSS,
	ID,
	INDEX, 	// Used for PageSelectElement Only
	LOCALIZEDCONTROLTYPE, //Used for Windows Element Only
	NAME,
	PARTIALTEXT,
	RUNTIMEID, //Used for Windows Element Only
	TAGNAME, //Used for Windows Element Only
	TEXT, 	// Used for both Element and Select Element
	VALUE, 	//Used for Select Element Only
	XYZ,
	XPATH;
	public static SelectorType of(String name) {
		return valueOf(name.toUpperCase());
	}
}
