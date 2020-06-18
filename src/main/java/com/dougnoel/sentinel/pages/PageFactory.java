package com.dougnoel.sentinel.pages;

import java.util.HashMap;
/**
 * The Page Factory is a factory method that simply takes a string containing the name of a 
 * Page Object and returns the object to be worked on.
 */
public class PageFactory {
	private static HashMap<String, Page> pages = new HashMap<>();	
	
	private PageFactory() {
		//Exists only to defeat instantiation.
	}
	
	/**
	 * Returns the Page Object for the page name. This allows us to operate on pages
	 * without knowing they exist when we write step definitions.
	 * 
	 * @param pageName String the name of the page object
	 * @return Page the page object
	 */
	public static Page buildOrRetrievePage(String pageName) {
		Page page = pages.get(pageName);
		if (page != null) {
			return page;
		} else {
			page = new Page(pageName);
		}
		pages.put(pageName, page);
		return page;
	}

}