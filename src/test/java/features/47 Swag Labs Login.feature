#language: en
#Author: Doug Noël
@47
Feature: 47 Swag Labs Login
  As a Swag Labs customer who is not locked out, 
  I need to be able to log in,
  so that I can purchase Sauce Labs merch.
  
  @47A  
  Scenario: 47A Successful Login
  	Given I am on the Sauce Demo Login Page
  	When I fill the account information for account StandardUser into the Username field and the Password field
  	  And I click the Login Button
  	Then I am redirected to the Sauce Demo Main Page
  	  And I verify the App Logo exists
  
  @47B		
  Scenario: 47B Failed Login with Locked Out User
	Given I am on the Sauce Demo Login Page
  	When I fill the account information for account LockedOutUser into the Username field and the Password field
  	  And I click the Login Button
  	Then I verify the Error Message contains the text "Sorry, this user has been locked out."