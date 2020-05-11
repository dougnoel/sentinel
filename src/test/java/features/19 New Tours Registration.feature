#Author: Doug Noël
@19
Feature: 19 New Tours Registration
  As a new user of the New Tours website,
  I want to be able to register, 
  so that I can store my personal information.
  
  @20  
  Scenario: 20 Successful Registration
  	Given I am on the New Tours Login Page
  	When I click the Register Menu Link
  		And I am redirected to the New Tours Registration Page
  		And I enter Bob in the First Name Field
  		And I enter Smith in the Last Name Field
  		And I enter bobsmith in the Username Field
  		And I enter passw0rd in the Password Field
  		And I enter passw0rd in the Confirm Password Field
  		And I click the Submit Button
		Then I am redirected to the New Tours Registration Confirmation Page
			And I verify the Registration Header exists