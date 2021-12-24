package com.dougnoel.sentinel.enums;

/**
 * Currently only web page or executable
 */
public enum PageObjectType {
	WEBPAGE,
	EXECUTABLE;
	public static PageObjectType of(String name) {
		return valueOf(name.toUpperCase());
	}
}
