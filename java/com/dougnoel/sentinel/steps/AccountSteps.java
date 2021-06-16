package com.dougnoel.sentinel.steps;

import com.dougnoel.sentinel.configurations.Configuration;
import io.cucumber.java.en.When;

public class AccountSteps {

	private static final String DEFAULT = "default";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";
    
    /**
     * Looks up the given account in the page object yaml file based on the environment and enters the user name and password 
     * information into the specified fields.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I fill the account information for account DoctorBob into the user name field and password field</li>
     * <li>I fill the account information for account Tester into the username box and the password box</li>
     * <li>I fill the account information for account SystemUser into the user name and the password</li>
     * </ul>
     * @param account String user account to look up for the page object being used
     * @param usernameField String user name field element to enter the user name
     * @param passwordField String password field element to enter the password
     */
    @When("I fill the account information for account {} into the {} and the {}")
    public static void fillAccountInfoIntoUsernameAndPasswordFields(String account, String usernameField, String passwordField) {
    	TextSteps.enterText(Configuration.accountInformation(account, USERNAME), usernameField);
    	TextSteps.enterText(Configuration.accountInformation(account, PASSWORD), passwordField);          
    }

    /**
     * Looks up the default account in the page object yaml file based on the environment and enters the user name and password 
     * information into the specified fields.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I fill the account information into the user name field and password field</li>
     * <li>I fill the account information into the username box and the password box</li>
     * <li>I fill the account information into the user name and the password</li>
     * </ul>     
     * @param usernameField String user name field element to enter the user name
     * @param passwordField String password field element to enter the password
     */
    @When("I fill the account information into the {} and the {}")
    public static void fillAccountInfoIntoUsernameAndPasswordFields(String usernameField, String passwordField) {
    	fillUsernameField(usernameField);
    	fillPasswordField(passwordField);         
    }
    
    /**
     * Looks up the given account in the page object yaml file based on the environment and enters the password 
     * information into the specified field.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I fill the password for account DoctorBob into the pass word field</li>
     * <li>I fill the password for account Tester into the password box</li>
     * <li>I fill the password for account SystemUser into the password field</li>
     * </ul>
     * @param account String user account to look up for the page object being used
     * @param passwordField String password field element to enter the password
     */
    @When("I fill the password for account {} into the {}")
    public static void fillPasswordField(String account, String passwordField) {
        	TextSteps.enterText(Configuration.accountInformation(account, PASSWORD), passwordField);         
    }

    /**
     * Looks up the default account in the page object yaml file based on the environment and enters the password 
     * information into the specified field.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I fill the password into the pass word field</li>
     * <li>I fill the password into the password box</li>
     * <li>I fill the password into the password field</li>
     * </ul>
     * @param passwordField String password field element to enter the password
     */
    @When("I fill the password into the {}")
    public static void fillPasswordField(String passwordField) {
    	fillPasswordField(DEFAULT, passwordField);
    }
    
    /**
     * Looks up the given account in the page object yaml file based on the environment and enters the user name 
     * information into the specified field.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I fill the username for account DoctoBob into the username field</li>
     * <li>I fill the username for account Tester into the username box</li>
     * <li>I fill the username for account SystemUser into the User Name field</li>
     * </ul>
     * @param account String user account to look up for the page object being used
     * @param usernameField String user name field element to enter the user name
     */
    @When("I fill the username for account {} into the {}")
    public static void fillUsernameField(String account, String usernameField) {
        TextSteps.enterText(Configuration.accountInformation(account, USERNAME), usernameField);
    }
 
    /**
     * Looks up the default account in the page object yaml file based on the environment and enters the user name 
     * information into the specified field.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I fill the username into the username field</li>
     * <li>I fill the username into the username box</li>
     * <li>I fill the username into the User Name field</li>
     * </ul>
     * @param usernameField String user name field element to enter the user name
     */
    @When("I fill the username into the {}")
    public static void fillUsernameField(String usernameField) {
    	fillUsernameField(DEFAULT, usernameField);
    }
}
