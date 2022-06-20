#language: en
#Author: Doug Noël

Feature: Text Verifcation Tests
  Testing to make sure text verification works.

  @text
  Scenario: Text Verification
    Given I am on the Internet Page
      And I wait 0.001 seconds
    Then I wait until the Form Authentication link contains the text "Form Authentication"
      And I wait until the Form Authentication link does not contain the text "Zoboomafoo"
    When I click the Form Authentication link
      And I am redirected to the Internet Login Page
    Then I verify the username field is empty
    When I fill the account information into the username field and the password field
      And I enter Bob in the username field
      And I randomly enter text in the password field
      And I reuse the password field text in the password field
    Then I verify the username field is not empty
      And I verify the username field does not contain the text "foo"
      And I verify the username field has the text "Bob"
    When I press the browser back button
      And I press the browser forward button
      And I press the browser refresh button

  @text
  Scenario: I randomly append/prepend/enter text, and verify the entry
    Given I am on the Guinea Pig Page
    When I randomly enter test in the Comments Text Area
      And I click the Send Button
    Then I wait until the Your Comments Span contains the text entered for the Comments Text Area
    When I clear the Comments Text Area
      And I uniquely enter test in the Comments Text Area
      And I click the Send Button
    Then I wait until the Your Comments Span contains the text entered for the Comments Text Area
    When I clear the Comments Text Area
      And I randomly prepend @gmail.com in the Email Text Area
    Then I wait until the Email Text Area contains the text entered in the Email Text Area
    When I clear the Comments Text Area
      And I randomly append to @gmail.com in the Comments Text Area
      And I click the Send Button
      And I wait until the Comments Text Area contains the text used in the Comments Text Area
    Then I wait until the Comments Text Area does not contain the text used in the Email Text Area