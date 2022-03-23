#language: en
#Author: Doug Noël
Feature: Text Verifcation Tests
  Testing to make sure text verification works.
  
  @text 
  Scenario: Text Verification
  	Given I am on the Internet Page
  	  And I wait 0.001 seconds
  	Then I wait until the Form Authentication link contains the text "Form Authentication"
  	When I click the Form Authentication link
  	  And I am redirected to the Internet Login Page
  	Then I verify the username field is empty  
  	When I fill the account information into the username field and the password field
  	  And I enter Bob in the username field
  	  And I randomly enter text in the password field for the fun of it
  	  And I reuse the password field text in the password field
  	Then I verify the username field is not empty
  	  And I verify the username field does not contain the text "foo"
  	  And I verify the username field has the text "Bob"
  	When I press the browser back button
  	  And I press the browser forward button
  	  And I press the browser refresh button