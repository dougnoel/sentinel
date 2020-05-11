package com.demoaut;

import com.dougnoel.sentinel.elements.Div;

/**
 * This is an example of a page object for a confirmation page.
 */
public class NewToursRegistrationConfirmationPage extends NewToursMenuPage {
	public Div registration_header() { return new Div(XPATH, "//img[contains(@src, 'mast_register.gif')]"); }
}
