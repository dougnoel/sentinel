#language: en
#Author: Tyler Bouchard

@math
Feature: Math Verification Tests
  Testing to make sure mathematical verifications work

  Scenario: Verify added whole number rounded text and value
    Given I am on the Calculator Page
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text input as "starting number"
      And I click the plus button
      And I click the dot button
      And I click the 4 button
      And I click the equals button
    Then I verify the numerical text of the rounded text div is .4 greater than the previously stored starting number value rounded to 0 decimal places
      And I verify the numerical value of the rounded value input is .4 greater than the previously stored starting number value rounded to 0 decimal places
      And I verify the rounded text div has the text "3"
      And I verify the rounded value input with the attribute value has the value 3

  Scenario: Verify subtracted whole number rounded text and value
    Given I am on the Calculator Page
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text input as "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 4 button
      And I click the equals button
    Then I verify the numerical text of the rounded text div is .4 less than the previously stored starting number value rounded to 0 decimal places
      And I verify the numerical value of the rounded value input is 0.4 less than the previously stored starting number value rounded to 0 decimal places
      And I verify the rounded text div has the text "2"
      And I verify the rounded value input with the attribute value has the value 2

  Scenario: Verify added whole number truncated text and value
    Given I am on the Calculator Page
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text input as "starting number"
      And I click the plus button
      And I click the 1 button
      And I click the equals button
    Then I verify the numerical text of the truncated text div is 1.0 greater than the previously stored starting number value truncated to 0 decimal places
      And I verify the numerical value of the truncated value input is 1 greater than the previously stored starting number value truncated to 0 decimal places
      And I verify the truncated text div has the text "3"
      And I verify the truncated value input with the attribute value has the value 3

  Scenario: Verify subtracted whole number truncated text and value
    Given I am on the Calculator Page
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text input as "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 6 button
      And I click the equals button
    Then I verify the numerical text of the truncated text div is .6 less than the previously stored starting number value truncated to 0 decimal places
      And I verify the numerical value of the truncated value input is .6 less than the previously stored starting number value truncated to 0 decimal places
      And I verify the truncated text div has the text "1"
      And I verify the truncated value input with the attribute value has the value 1

  Scenario: Verify added decimal rounded text and value
    Given I am on the Calculator Page
      And I enter 2 in the decimal length input
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text input as "starting number"
      And I click the plus button
      And I click the dot button
      And I click the 4 button
      And I click the 4 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the rounded text div is .45 greater than the previously stored starting number value rounded to 3 decimal places
      And I verify the numerical value of the rounded value input is .45 greater than the previously stored starting number value rounded to 3 decimal places
      And I verify the rounded text div has the text "3"
      And I verify the rounded value input with the attribute value has the value 3

  Scenario: Verify subtracted whole number rounded text and value
    Given I am on the Calculator Page
    When I click the 2 button
    And I click the dot button
    And I click the 5 button
    And I click the equals button
    And I store the text in the raw text input as "starting number"
    And I click the minus button
    And I click the dot button
    And I click the 4 button
    And I click the equals button
    Then I verify the numerical text of the rounded text div is .4 less than the previously stored starting number value rounded to 0 decimal places
    And I verify the numerical value of the rounded value input is 0.4 less than the previously stored starting number value rounded to 0 decimal places
    And I verify the rounded text div has the text "2"
    And I verify the rounded value input with the attribute value has the value 2

  Scenario: Verify added whole number truncated text and value
    Given I am on the Calculator Page
    When I click the 2 button
    And I click the dot button
    And I click the 5 button
    And I click the equals button
    And I store the text in the raw text input as "starting number"
    And I click the plus button
    And I click the 1 button
    And I click the equals button
    Then I verify the numerical text of the truncated text div is 1.0 greater than the previously stored starting number value truncated to 0 decimal places
    And I verify the numerical value of the truncated value input is 1 greater than the previously stored starting number value truncated to 0 decimal places
    And I verify the truncated text div has the text "3"
    And I verify the truncated value input with the attribute value has the value 3

  Scenario: Verify subtracted whole number truncated text and value
    Given I am on the Calculator Page
    When I click the 2 button
    And I click the dot button
    And I click the 5 button
    And I click the equals button
    And I store the text in the raw text input as "starting number"
    And I click the minus button
    And I click the dot button
    And I click the 6 button
    And I click the equals button
    Then I verify the numerical text of the truncated text div is .6 less than the previously stored starting number value truncated to 0 decimal places
    And I verify the numerical value of the truncated value input is .6 less than the previously stored starting number value truncated to 0 decimal places
    And I verify the truncated text div has the text "1"
    And I verify the truncated value input with the attribute value has the value 1