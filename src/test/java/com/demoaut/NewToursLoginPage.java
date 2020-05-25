package com.demoaut;

import com.dougnoel.sentinel.elements.Button;
import com.dougnoel.sentinel.elements.Textbox;

/**
 * This is an example of a page object for a login page.
 * Notice that there is a matching NewToursLoginPage.yml file that contains the URL for the page
 * and the login information for the test user.
 */
public class NewToursLoginPage extends NewToursMenuPage {
	public Textbox username_field() { return new Textbox(NAME, "userName"); }
	public Textbox password_field() { return new Textbox(NAME, "password"); }
	public Button sign_in_button() { return new Button(NAME, "login"); } //Calling this the Sign In Button because that matches the text on the button to make it clear to users
}
