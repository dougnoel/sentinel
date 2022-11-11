@287
Feature: Select Verification Steps
  Tests the select verification steps

  Background:
    Given I am on the Dropdown Page

  @287A
  Scenario: Verify Dropdown Has Option
    Then I verify the dropdown has the option Option 1

  @287B
  Scenario: Verify Dropdown Does Not Have Option
    Then I verify the dropdown does not have the option Option 9
    
  @287C
  Scenario: Verify Dropdown Has Currently Selected Option
    Then I verify the dropdown has the text "Please select an option"
      And I verify the dropdown is not empty
    
  @287D
  Scenario: Verify Dropdown Does Not Have Currently Selected Option
    Then I verify the dropdown does not have the text "Option 2"
    
  @287E
  Scenario: Verify Dropdown Has Number Of Options
    Then I verify the dropdown has 3 options
  
  @287F
  Scenario: Verify Dropdown Does Not Have Number Of Options
    Then I verify the dropdown does not have 1 option
  