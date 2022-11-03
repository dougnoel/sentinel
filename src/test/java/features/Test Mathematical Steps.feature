#language: en
#Author: Tyler Bouchard

@math
Feature: Math Verification Tests
  Testing to make sure mathematical verifications work

  #Tests uncaring formatting
  Scenario: Verifies old and new value comparison when we:
  -Store the number 2.4
  -Add .1 on the webpage inputs
  That we correctly verify the old value is now 2.5 on the raw displays
    Given I am on the Calculator Page
    When I click the 2 button
      And I click the dot button
      And I click the 4 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the plus button
      And I click the dot button
      And I click the 1 button
      And I click the equals button
    Then I verify the numerical text of the raw text div is .1 greater than the previously stored starting number value
      And I verify the numerical value of the raw value input is .1 greater than the previously stored starting number value
      And I verify the raw text div has the text "2.5"
      And I verify the raw value input with the attribute value has the value 2.5

  Scenario: Verifies old and new value comparison when we:
  -Store the number 2.5
  -Subtract .1 on the webpage inputs
  That we correctly verify the old value is now 2.4 on the raw displays
    Given I am on the Calculator Page
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 1 button
      And I click the equals button
    Then I verify the numerical text of the raw text div is .1 less than the previously stored starting number value
      And I verify the numerical value of the raw value input is .1 less than the previously stored starting number value
      And I verify the raw text div has the text "2.4"
      And I verify the raw value input with the attribute value has the value 2.4

  Scenario: Verifies old and new value comparison when we:
  -Store the number 2.5
  -Add .3005 on the webpage inputs
  That we correctly verify the old value has been changed to 2.8005 on the raw displays
    Given I am on the Calculator Page
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the plus button
      And I click the dot button
      And I click the 3 button
      And I click the 0 button
      And I click the 0 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the raw text div is .3005 greater than the previously stored starting number value
      And I verify the numerical value of the raw value input is 0.3005 greater than the previously stored starting number value
      And I verify the raw text div has the text "2.8005"
      And I verify the raw value input with the attribute value has the value 2.8005

  Scenario: Verifies old and new value comparison when we:
  -Store the number -2.5
  -Subtract .3005 on the webpage inputs
  That we correctly verify the old value has been changed to -2.8005 on the raw displays
    Given I am on the Calculator Page
      And I click the sign button
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 3 button
      And I click the 0 button
      And I click the 0 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the raw text div is .3005 less than the previously stored starting number value
      And I verify the numerical value of the raw value input is 0.3005 less than the previously stored starting number value
      And I verify the raw text div has the text "-2.8005"
      And I verify the raw value input with the attribute value has the value -2.8005

  Scenario: Verifies old and new value comparison when we:
  -Store the number -2.5
  -Add .3005 on the webpage inputs
  That we correctly verify the old value has been changed to -2.1995 on the raw displays
    Given I am on the Calculator Page
      And I click the sign button
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the plus button
      And I click the dot button
      And I click the 3 button
      And I click the 0 button
      And I click the 0 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the raw text div is .3005 greater than the previously stored starting number value
      And I verify the numerical value of the raw value input is 0.3005 greater than the previously stored starting number value
      And I verify the raw text div has the text "-2.1995"
      And I verify the raw value input with the attribute value has the value -2.1995

  #Test positives
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
      And I store the text in the raw text div as a variable with the name "starting number"
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
      And I store the text in the raw text div as a variable with the name "starting number"
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
      And I store the text in the raw text div as a variable with the name "starting number"
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
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 1 button
      And I click the equals button
    Then I verify the numerical text of the truncated text div is .1 less than the previously stored starting number value truncated to 0 decimal places
      And I verify the numerical value of the truncated value input is .1 less than the previously stored starting number value truncated to 0 decimal places
      And I verify the truncated text div has the text "2"
      And I verify the truncated value input with the attribute value has the value 2

  Scenario: Verifies old and new value comparison when we:
  -Store the number 2.5
  -Add .3005 on the webpage inputs
  -Tell the output on page to display a 3 decimal places in length
  That we correctly verify the old value has been changed to 2.8005 and rounded to 2.801 on the rounded number displays
    Given I am on the Calculator Page
      And I enter 3 in the decimal length input
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the plus button
      And I click the dot button
      And I click the 3 button
      And I click the 0 button
      And I click the 0 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the rounded text div is .3005 greater than the previously stored starting number value rounded to 3 decimal places
      And I verify the numerical value of the rounded value input is 0.3005 greater than the previously stored starting number value rounded to 3 decimal places
      And I verify the rounded text div has the text "2.801"
      And I verify the rounded value input with the attribute value has the value 2.801

  Scenario: Verifies old and new value comparison when we:
  -Store the number 2.5
  -Subtract .3005 on the webpage inputs
  -Tell the output on page to display a 3 decimal places in length
  That we correctly verify the old value has been changed to 2.1995 and rounded to 2.200 on the rounded number displays
    Given I am on the Calculator Page
      And I enter 3 in the decimal length input
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 3 button
      And I click the 0 button
      And I click the 0 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the rounded text div is .3005 less than the previously stored starting number value rounded to 3 decimal places
      And I verify the numerical value of the rounded value input is 0.3005 less than the previously stored starting number value rounded to 3 decimal places
      And I verify the rounded text div has the text "2.200"
      And I verify the rounded value input with the attribute value has the value 2.200

  Scenario: Verifies old and new value comparison when we:
  -Store the number 2.5
  -Add .3005 on the webpage inputs
  -Tell the output on page to display a 3 decimal places in length
  That we correctly verify the old value has been changed to 2.8005 and truncated to 2.800 on the truncated number displays
    Given I am on the Calculator Page
      And I enter 3 in the decimal length input
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the plus button
      And I click the dot button
      And I click the 3 button
      And I click the 0 button
      And I click the 0 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the truncated text div is .3005 greater than the previously stored starting number value truncated to 3 decimal places
      And I verify the numerical value of the truncated value input is 0.3005 greater than the previously stored starting number value truncated to 3 decimal places
      And I verify the truncated text div has the text "2.800"
      And I verify the truncated value input with the attribute value has the value 2.800

  Scenario: Verifies old and new value comparison when we:
  -Store the number 2.5
  -Subtract .3005 on the webpage inputs
  -Tell the output on page to display a 3 decimal places in length
  That we correctly verify the old value has been changed to 2.1995 and truncated to 2.199 on the truncated number displays
    Given I am on the Calculator Page
      And I enter 3 in the decimal length input
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 3 button
      And I click the 0 button
      And I click the 0 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the truncated text div is .3005 less than the previously stored starting number value truncated to 3 decimal places
      And I verify the numerical value of the truncated value input is 0.3005 less than the previously stored starting number value truncated to 3 decimal places
      And I verify the truncated text div has the text "2.199"
      And I verify the truncated value input with the attribute value has the value 2.199

  #Test Negatives
  Scenario: Verifies old and new value comparison when we:
  -Store the number -2.4
  -Subtract .1 on the webpage inputs
  -Tell the output on page to display a whole number
  That we correctly verify the old value has been changed to -2.5 and rounded to -3 on the rounded number displays
    Given I am on the Calculator Page
      And I click the sign button
    When I click the 2 button
      And I click the dot button
      And I click the 4 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 1 button
      And I click the equals button
    Then I verify the numerical text of the rounded text div is .1 less than the previously stored starting number value rounded to 0 decimal places
      And I verify the numerical value of the rounded value input is .1 less than the previously stored starting number value rounded to 0 decimal places
      And I verify the rounded text div has the text "-3"
      And I verify the rounded value input with the attribute value has the value -3

  Scenario: Verifies old and new value comparison when we:
  -Store the number -2.5
  -Add .1 on the webpage inputs
  -Tell the output on page to display a whole number
  That we correctly verify the old value has been changed to -2.4 and rounded to -2 on the rounded number displays
    Given I am on the Calculator Page
      And I click the sign button
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the plus button
      And I click the dot button
      And I click the 1 button
      And I click the equals button
    Then I verify the numerical text of the rounded text div is .1 greater than the previously stored starting number value rounded to 0 decimal places
      And I verify the numerical value of the rounded value input is 0.1 greater than the previously stored starting number value rounded to 0 decimal places
      And I verify the rounded text div has the text "-2"
      And I verify the rounded value input with the attribute value has the value -2

  Scenario: Verifies old and new value comparison when we:
  -Store the number -2.4
  -Subtract .1 on the webpage inputs
  -Tell the output on page to display a whole number
  That we correctly verify the old value has been changed to -2.5 and truncated to -3 on the truncated number displays
    Given I am on the Calculator Page
      And I click the sign button
    When I click the 2 button
      And I click the dot button
      And I click the 4 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 1 button
      And I click the equals button
    Then I verify the numerical text of the truncated text div is .1 less than the previously stored starting number value truncated to 0 decimal places
      And I verify the numerical value of the truncated value input is .1 less than the previously stored starting number value truncated to 0 decimal places
      And I verify the truncated text div has the text "-2"
      And I verify the truncated value input with the attribute value has the value -2

  Scenario: Verifies old and new value comparison when we:
  -Store the number -2.5
  -Add .1 on the webpage inputs
  -Tell the output on page to display a whole number
  That we correctly verify the old value has been changed to -2.4 and truncated to -2 on the truncated number displays
    Given I am on the Calculator Page
      And I click the sign button
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the plus button
      And I click the dot button
      And I click the 1 button
      And I click the equals button
    Then I verify the numerical text of the truncated text div is .1 greater than the previously stored starting number value truncated to 0 decimal places
      And I verify the numerical value of the truncated value input is .1 greater than the previously stored starting number value truncated to 0 decimal places
      And I verify the truncated text div has the text "-2"
      And I verify the truncated value input with the attribute value has the value -2

  Scenario: Verifies old and new value comparison when we:
  -Store the number -2.4
  -Subtracts .445 on the webpage inputs
  -Tell the output on page to display a 2 decimal places in length
  That we correctly verify the old value has been changed to -2.8449~ and rounded to -2.84 on the rounded number displays
    Given I am on the Calculator Page
      And I click the sign button
      And I enter 2 in the decimal length input
    When I click the 2 button
      And I click the dot button
      And I click the 4 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 4 button
      And I click the 4 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the rounded text div is .445 less than the previously stored starting number value rounded to 2 decimal places
      And I verify the numerical value of the rounded value input is .445 less than the previously stored starting number value rounded to 2 decimal places
      And I verify the rounded text div has the text "-2.84"
      And I verify the rounded value input with the attribute value has the value -2.84

  Scenario: Verifies old and new value comparison when we:
  -Store the number -2.5
  -Add .3005 on the webpage inputs
  -Tell the output on page to display a 3 decimal places in length
  That we correctly verify the old value has been changed to -2.1995 and rounded to -2.200 on the rounded number displays
    Given I am on the Calculator Page
      And I enter 3 in the decimal length input
      And I click the sign button
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the plus button
      And I click the dot button
      And I click the 3 button
      And I click the 0 button
      And I click the 0 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the rounded text div is .3005 greater than the previously stored starting number value rounded to 3 decimal places
      And I verify the numerical value of the rounded value input is 0.3005 greater than the previously stored starting number value rounded to 3 decimal places
      And I verify the rounded text div has the text "-2.200"
      And I verify the rounded value input with the attribute value has the value -2.200

  Scenario: Verifies old and new value comparison when we:
  -Store the number -2.5
  -Subtracts .3005 on the webpage inputs
  -Tell the output on page to display a 3 decimal places in length
  That we correctly verify the old value has been changed to -2.8005 and truncated to -2.800 on the truncated number displays
    Given I am on the Calculator Page
      And I enter 3 in the decimal length input
      And I click the sign button
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the minus button
      And I click the dot button
      And I click the 3 button
      And I click the 0 button
      And I click the 0 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the truncated text div is .3005 less than the previously stored starting number value truncated to 3 decimal places
      And I verify the numerical value of the truncated value input is 0.3005 less than the previously stored starting number value truncated to 3 decimal places
      And I verify the truncated text div has the text "-2.800"
      And I verify the truncated value input with the attribute value has the value -2.800

  Scenario: Verifies old and new value comparison when we:
  -Store the number -2.5
  -Add .3005 on the webpage inputs
  -Tell the output on page to display a 3 decimal places in length
  That we correctly verify the old value has been changed to -2.1995 and truncated to -2.199 on the truncated number displays
    Given I am on the Calculator Page
      And I enter 3 in the decimal length input
      And I click the sign button
    When I click the 2 button
      And I click the dot button
      And I click the 5 button
      And I click the equals button
      And I store the text in the raw text div as a variable with the name "starting number"
      And I click the plus button
      And I click the dot button
      And I click the 3 button
      And I click the 0 button
      And I click the 0 button
      And I click the 5 button
      And I click the equals button
    Then I verify the numerical text of the truncated text div is .3005 greater than the previously stored starting number value truncated to 3 decimal places
      And I verify the numerical value of the truncated value input is 0.3005 greater than the previously stored starting number value truncated to 3 decimal places
      And I verify the truncated text div has the text "-2.199"
      And I verify the truncated value input with the attribute value has the value -2.199