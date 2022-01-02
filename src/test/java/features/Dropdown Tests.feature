#Author: Doug Noël
@example @dropdown
Feature: Dropdown Tests
  Testing the Dropdown Object Functionality

  @273
  Scenario: 273 Dropdown
    Given I am on the Dropdown Page
    When I select Option 1 from the Dropdown
      And I verify the Dropdown has the text "Option 1" selected
      And I select the 2nd option from the Dropdown
    Then I verify the Dropdown has the text "Option 2" selected
      And I verify the Dropdown has the value selected for the Dropdown
      But I verify the Dropdown does not have the text "Twenty" selected
	
  @22
  Scenario: 22 Prime NG Dropdown
    Given I am on the Prime NG Dropdown Page
    When I select New York from the City Dropdown
    Then I verify the City Dropdown has the text "New York" selected
      And I press the escape key
    When I select the 2nd option from the City Dropdown
    Then I verify the City Dropdown has the text "Rome" selected
      And I verify the City Dropdown has the value selected for the City Dropdown
      But I verify the City Dropdown does not have the text "New York" selected
    When I select Australia from the Country Dropdown
    Then I verify the City Dropdown does not have the value selected for the Country Dropdown

  @84
  Scenario: 84 Material UI Select
    Given I am on the Material UI Select Page
    When I select Twenty from the Age Dropdown
    	And I verify the Age Dropdown has the text "Twenty" selected
    	And I select the 3rd option from the Age Dropdown
    Then I verify the Age Dropdown has the text "Thirty" selected
      And I verify the Age Dropdown has the value selected for the Age Dropdown
      But I verify the Age Dropdown does not have the text "Twenty" selected