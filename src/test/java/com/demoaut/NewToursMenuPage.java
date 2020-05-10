package com.demoaut;

import com.dougnoel.sentinel.elements.Link;
import com.dougnoel.sentinel.pages.Page;

/**
 * This is an example of a menu page object. It does not have an associated url, as it is actually a page part.
 * Other pages inherit from this page part, so that you can use the menu on any page.
 */
public class NewToursMenuPage extends Page {
	public Link register_menu_link() { return new Link(TEXT, "REGISTER"); }
}
