#language: en
#Author: Tyler Bouchard

@math
Feature: Math Verification Tests
  Testing to make sure mathematical verifications work

  Scenario: Verifies old and new value comparison when we:
  -Store the number 2.4
  -Add .1 on the webpage inputs
  -Tell the output on page to display a whole number
  That we correctly verify the old value has been changed to 2.5 and rounded to 3 on the rounded number displays
    Given I am on the Calculator Page
    When I click the 2 button
      And I click the dot button
      And I click the 4 button
      And I click the equals button
      And I store the text in the raw text input as "starting number"
      And I click the plus button
      And I click the dot button
      And I click the 1 button
      And I click the equals button
    Then I verify the numerical text of the rounded text div is .1 greater than the previously stored starting number value rounded to 0 decimal places
      And I verify the numerical value of the rounded value input is .1 greater than the previously stored starting number value rounded to 0 decimal places
      And I verify the rounded text div has the text "3"
      And I verify the rounded value input with the attribute value has the value 3

  Scenario: Verifies old and new value comparison when we:
  -Store the number 2.5
  -Subtract .1 on the webpage inputs
  -Tell the output on page to display a whole number
  That we correctly verify the old value has been changed to 2.4 and rounded to 2 on the rounded number displays
    Given I am on the Calculator Page
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text input as "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 1 button
      And I click the equals button
    Then I verify the numerical text of the rounded text div is .1 less than the previously stored starting number value rounded to 0 decimal places
      And I verify the numerical value of the rounded value input is 0.1 less than the previously stored starting number value rounded to 0 decimal places
      And I verify the rounded text div has the text "2"
      And I verify the rounded value input with the attribute value has the value 2

  Scenario: Verifies old and new value comparison when we:
  -Store the number 2.4
  -Add .1 on the webpage inputs
  -Tell the output on page to display a whole number
  That we correctly verify the old value has been changed to 2.5 and truncated to 2 on the truncated number displays
    Given I am on the Calculator Page
    When I click the 2 button
      And I click the dot button
      And I click the 4 button
      And I click the equals button
      And I store the text in the raw text input as "starting number"
      And I click the plus button
      And I click the dot button
      And I click the 1 button
      And I click the equals button
    Then I verify the numerical text of the truncated text div is .1 greater than the previously stored starting number value truncated to 0 decimal places
      And I verify the numerical value of the truncated value input is .1 greater than the previously stored starting number value truncated to 0 decimal places
      And I verify the truncated text div has the text "2"
      And I verify the truncated value input with the attribute value has the value 2

  Scenario: Verifies old and new value comparison when we:
  -Store the number 2.5
  -Subtract .1 on the webpage inputs
  -Tell the output on page to display a whole number
  That we correctly verify the old value has been changed to 2.4 and truncated to 2 on the truncated number displays
    Given I am on the Calculator Page
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text input as "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 1 button
      And I click the equals button
    Then I verify the numerical text of the truncated text div is .1 less than the previously stored starting number value truncated to 0 decimal places
      And I verify the numerical value of the truncated value input is .1 less than the previously stored starting number value truncated to 0 decimal places
      And I verify the truncated text div has the text "2"
      And I verify the truncated value input with the attribute value has the value 2

  Scenario: Verifies old and new value comparison when we:
  -Store the number 2.4
  -Add .445 on the webpage inputs
  -Tell the output on page to display a 2 decimal places in length
  That we correctly verify the old value has been changed to 2.8449~ and rounded to 2.85 on the rounded number displays
    Given I am on the Calculator Page
      And I enter 2 in the decimal length input
    When I click the 2 button
      And I click the dot button
      And I click the 4 button
      And I click the equals button
      And I store the text in the raw text input as "starting number"
      And I click the plus button
      And I click the dot button
      And I click the 4 button
      And I click the 4 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the rounded text div is .445 greater than the previously stored starting number value rounded to 2 decimal places
      And I verify the numerical value of the rounded value input is .445 greater than the previously stored starting number value rounded to 2 decimal places
      And I verify the rounded text div has the text "2.85"
      And I verify the rounded value input with the attribute value has the value 2.85

  Scenario: Verify subtracted decimal rounded text and value
    Given I am on the Calculator Page
      And I enter 3 in the decimal length input
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text input as "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 3 button
      And I click the 0 button
      And I click the 0 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the rounded text div is .3 less than the previously stored starting number value rounded to 3 decimal places
      And I verify the numerical value of the rounded value input is 0.3 less than the previously stored starting number value rounded to 3 decimal places
      And I verify the rounded text div has the text "2.200"
      And I verify the rounded value input with the attribute value has the value 2.200

  Scenario: Verify added decimal truncated text and value
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
    Then I verify the numerical text of the rounded text div is .45 greater than the previously stored starting number value truncated to 2 decimal places
      And I verify the numerical value of the rounded value input is .45 greater than the previously stored starting number value truncated to 2 decimal places
      And I verify the truncated text div has the text "2.94"
      And I verify the truncated value input with the attribute value has the value 2.94

  Scenario: Verify subtracted decumal truncated text and value
    Given I am on the Calculator Page
      And I enter 3 in the decimal length input
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text input as "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 3 button
      And I click the 0 button
      And I click the 0 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the rounded text div is .3 less than the previously stored starting number value rounded to 3 decimal places
      And I verify the numerical value of the rounded value input is 0.3 less than the previously stored starting number value rounded to 3 decimal places
      And I verify the truncated text div has the text "2.199"
      And I verify the truncated value input with the attribute value has the value 2.199