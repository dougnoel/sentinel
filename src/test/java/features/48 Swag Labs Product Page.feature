#language: en
#Author: Doug Noël
@48
Feature: 48 Swag Labs Product Page
  As a user of the Swag Labs shop,
  I want to be able to sort products,
  so that I can find what I need.
  
  @48A
  Scenario: 48A Product Sort
  	Given I login to the Sauce Demo Login Page as StandardUser
  	When I select the 1st option from the Product Sort Dropdown
  	Then I verify the First Item Name contains the text "Test.allTheThings() T-Shirt (Red)"
  	When I select the 2nd option from the Product Sort Dropdown
  	Then I verify the First Item Name contains the text "Sauce Labs Onesie"
  	When I select the 3rd option from the Product Sort Dropdown
  	Then I verify the First Item Name contains the text "Sauce Labs Fleece Jacket"
  	
  @48B  
  Scenario: 48B Item Description Link
    Given I login to the Sauce Demo Login Page as StandardUser
  	When I select the 1st option from the Product Sort Dropdown
  	Then I verify the First Item Name contains the text "Test.allTheThings() T-Shirt (Red)"