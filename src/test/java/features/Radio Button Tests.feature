#Author: Doug Noël
@example @radiobutton
Feature: Radio Button Tests
  Testing the Table Page Object Functionality

  @html-radiobutton
  Scenario: HTML Radio Buttons
    Given I am on the Radio Button Page
    When I click the Male Radio Button
    Then I verify the Male Radio Button is checked
    When I click the Female Radio Button
    Then I verify the Female Radio Button is selected
    	But I verify the Male Radio Button is not checked
    	
  @#106
  Scenario: HTML Radio Buttons
    Given I am on the Radio Button Page
    When I click the Male Radio Button
    Then I verify the Male Radio Button is checked
    When I click the Female Radio Button
    Then I verify the Female Radio Button is selected
    	But I verify the Male Radio Button is not checked