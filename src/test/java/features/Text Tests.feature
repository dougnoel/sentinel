#language: en
#Author: Doug Noël

Feature: Text Verification Tests
  Testing to make sure text verification works.

  @text
  Scenario: Text Verification
    Given I am on the Internet Page
      And I wait 0.001 seconds
    Then I wait until the Form Authentication link contains the text "Form Authentication"
      And I wait until the Form Authentication link does not contain the text "Zoboomafoo"
    When I click the Form Authentication link
    Then I switch to the Internet Login Page with the title "The Internet"
      And I verify the tab has the title text "The Internet"
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
    Then I verify the tab does not contain the title text "Loading"

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

  @text
  Scenario: I check for disabled and enabled elements of multiple disabling types
    Given I am on the Textbox Page
    Then I verify the middle name field is disabled
      And I verify the first name field is disabled
      And I verify the last name field is enabled

  @text
  Scenario: I store and verify an elements inner text
    Given I am on the Guinea Pig Page
    When I store the text in the example div as "div inner text"
      And I store the text in the example link as "link inner text"
    Then I wait until the example div contains the text used in the div inner text
      And I wait until the example link does not contain the text used in the div inner text
      And I wait until the example div does not contain the text used in the link inner text

  @text
  Scenario: I store and verify an elements value attribute
    Given I am on the Guinea Pig Page
    When I store the value of the attribute "value" in the example textbox as a variable with the name "textbox value"
      And I store the value of the attribute "href" in the example link as a variable with the name "href value"
    Then I verify the example textbox with the attribute value contains the same value used for the textbox value
      And I verify the example textbox with the attribute value does not contain the same value used for the href value
      And I verify the example link with the attribute href has the same value used for the href value

  @text
  Scenario: I store and verify an elements class attribute
    Given I am on the Guinea Pig Page
    When I store the value of the attribute "class" in the example div as a variable with the name "div class"
      And I store the value of the attribute "class" in the example textbox as a variable with the name "textbox class"
    Then I verify the example textbox with the attribute class contains the same value used for the textbox class
      And I verify the example textbox with the attribute class does not contain the same value used for the div class
      And I verify the example div with the attribute class has the same value used for the div class