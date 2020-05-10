#Author: Doug Noël
@16
Feature: 16 New Tours Login
  As a user of the New Tours website,
  I want to be able to login, 
  so that I can access my personal information.
  
  As a user of the New Tours website, if I enter incorrect login information,
  I want to receive a registration link,
  so that I can create an account.
  
  @17  
  Scenario: 17 Successful Login
  	Given I am on the New Tours Login Page
  	When I fill the account information for account RegularUser into the Username field and the Password field
  		And I click the Sign In Button
  	Then I am redirected to the New Tours Sign On Page
  		And I verify the Sign On Header exists
  
  @18		
  Scenario: 18 Failed Login
		Given I am on the New Tours Login Page
  	When I fill the account information for account BadUser into the Username field and the Password field
  		And I click the Sign In Button
  	Then I am redirected to the New Tours Sign On Page
  		And I verify the Registration Form Link exists