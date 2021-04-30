#language: en
#Author: Doug Noël
Feature: Text Verifcation Tests
  Testing to make sure text verification works.
  
  @text 
  Scenario: Text Verification
  	Given I am on the Internet Page
  	  And I wait 0.001 seconds
  	When I click the Form Authentication link
  	  And I am redirected to the Internet Login Page
  	Then I verify the username field is empty
  	When I enter Bob in the username field
  	Then I verify the username field is not empty
  	  And I verify the username field does not contain the text "foo"
  	  And I verify the username field has the text "Bob"