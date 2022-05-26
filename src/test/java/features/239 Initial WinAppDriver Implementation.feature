#language: en
#Author: ty.pixelplane@gmail.com

@239
Feature: 239 Implement WinAppDriver to automate windows
  As a user I want to be able to automate Windows 10 OS,
  so that I can automate things that cannot be automated in the browser.

  @239A @TextEntry
  Scenario: 239A Use Notepad
    Given I open the Notepad App
    When I click the Text Editor field
      And I enter test in the Text Editor field
    Then I verify the Text Editor field contains the text "test"
    When I clear the Text Editor field
    Then I verify the Text Editor field is empty

  @239B @ColorChecking
  Scenario: 239B Check Colors in Notepad
    Given I switch to the Notepad App
    When I click the Text Editor field
    Then I verify the Text Editor field with the attribute color has the value #FFFFFF
    When I hover the file menu dropdown
    Then I verify the file menu dropdown with the attribute color has the value #E5F3FF

  @239D
  Scenario: 239D Switch Between Active Tabs and Pages Together
    Open a New Tab and Ensures it Opens
    Switch to the Previous Tab and Ensure We Have Switched
    Open a New Window and Ensures it Opens
    Switch to the New Tab and Ensures We Have Switched
    Switch to the New Window and Ensures We Have Switched
    Switch to the Original Window and Ensures We Have Switched

    Given I am on the Encode DNA Home Page
    When I click the Open New Tab Button
    Then I verify a new tab opens to the Encode DNA New Tab Page
      And I verify the Header contains the text "Window Opened in a New Tab"
    When I switch to the Encode DNA Home Page on the previous tab
      And I click the Open New Window Button
    Then I verify a new window opens the Encode DNA PopUp Window
      And I verify the Header contains the text "A New Popup Window"
    When I switch to the Encode DNA New Tab Page in the previous window
    Then I verify the Header contains the text "Window Opened in a New Tab"
    When I switch to the Encode DNA PopUp Window in the next window
    Then I verify the Header contains the text "A New Popup Window"
    When I switch to the Encode DNA New Tab Page in the previous window
    Then I verify the Header contains the text "Window Opened in a New Tab"

  @239E
  Scenario: 239E Verify notepad element and window screenshot conditions
    Takes a screenshot of a window
    Verifies the current state of the window unchanged matches the previous window screenshot
    Verifies when window is changed that the previous window screenshot no longer matches
    Takes a screenshot of an element
    Verifies the current state of the element unchanged matches the previous element screenshot
    Verifies when element is changed that the previous element screenshot no longer matches
    Given I switch to the Notepad App
      And I click the Text Editor field
      And I clear the Text Editor field
      And I verify the Text Editor field is empty
      And I take a screenshot of the window
    Then I verify the window matches the previous image
    When I enter test the window screenshot no longer matches in the Text Editor field
    Then I verify the window does not match the previous image
    When I take a screenshot of the Text Editor field
    Then I verify the Text Editor field matches the previous image
    When I take a screenshot of the Text Editor field
      And I clear the Text Editor field
      And I enter test the element screenshot no longer matches in the Text Editor field
    Then I verify the Text Editor field does not match the previous image