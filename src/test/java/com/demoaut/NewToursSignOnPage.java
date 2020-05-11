package com.demoaut;

import com.dougnoel.sentinel.elements.Div;
import com.dougnoel.sentinel.elements.Link;

/**
 * This is an example of a page object for a confirmation page after a user is logged in.
 * It has been named based on the banner reading, "Sign On" on the page.
 */
public class NewToursSignOnPage extends NewToursMenuPage {
	public Div sign_on_header() { return new Div(XPATH, "//img[contains(@src, 'mast_signon.gif')]"); }
	public Link registration_form_link() { return new Link(TEXT, "registration form"); }
}
