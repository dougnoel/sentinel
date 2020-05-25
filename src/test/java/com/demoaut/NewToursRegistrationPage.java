package com.demoaut;

import com.dougnoel.sentinel.elements.Button;
import com.dougnoel.sentinel.elements.Textbox;

/**
 * This is an example of a page object for a login page.
 * Notice that there is a matching NewToursLoginPage.yml file that contains the URL for the page
 * and the login information for the test user.
 */
public class NewToursRegistrationPage extends NewToursLoginPage {
	//Contact Information Section
	public Textbox first_name_field() { return new Textbox(NAME, "firstName"); }
	public Textbox last_name_field() { return new Textbox(NAME, "lastName"); }
	//User Information Section
	public Textbox username_field() { return new Textbox(ID, "email"); } // This overrides the field defined on the NewToursLoginPage
	//public Textbox password_field() { return new Textbox(ID, "email"); } //This is inherited from the NewToursLoginPage
	public Textbox confirm_password_field() { return new Textbox(NAME, "confirmPassword"); }
	public Button submit_button() { return new Button(NAME, "register"); }
}
