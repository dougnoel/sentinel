#language: en
#Author: ty.pixelplane@gmail.com

@239
Feature: 239 Implement WinAppDriver to automate windows
  As a user I want to be able to automate Windows 10 OS, 
  so that I can automate things that cannot be automated in the browser.
  
  @239A @TextEntry
  Scenario: 239A Use Notepad
  	Given I open the Notepad App
  	When I enter test in the Text Editor field
  	Then I verify the Text Editor field contains the text "test"
  		And I clear the Text Editor field

  @239B @ButtonInteraction
  Scenario: 239B Use Calculator
  	Given I open the Calculator Program
  	When I click the one button
  	  And I click the Add button
  	  And I click the two button
  	  And I click the Equals button
  	Then I verify the Result contains the text "3"

  @239C @ColorChecking
  Scenario: 239C Check Colors in Notepad
  	Given I open the Notepad App
	When I click the Text Editor field
  	Then I verify the Text Editor field with the attribute color has the value #FFFFFF
	Then I verify the status bar with the attribute color has the value #F0F0F0