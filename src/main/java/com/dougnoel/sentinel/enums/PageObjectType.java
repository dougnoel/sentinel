package com.dougnoel.sentinel.enums;

/**
 * The possible types a PageObject can be.
 * 
 * WEBPAGE
 * EXECUTABLE
 * UNKNOWN
 */
public enum PageObjectType {
	WEBPAGE,
	EXECUTABLE,
	UNKNOWN;
	
	public static PageObjectType of(String name) {
		return valueOf(name.toUpperCase());
	}
}
