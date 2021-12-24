#language: en
#Author: ty.pixelplane@gmail.com

@239
Feature: 239 Implement WinAppDriver to automate windows
  As a user I want to be able to automate Windows 10 OS, 
  so that I can automate things that cannot be automated in the browser.
  
  @239A @TextEntry  
  Scenario: 239A Use Notepad
  	Given that I open "Notepad Main Page"
  	When I enter test in the Text Editor field
  	#When I enter "test" into the "Text Editor" field
  	Then I expect that "Text Editor" contains "test"

  @239B @ButtonInteraction
  Scenario: 239B Use Calculator
  	Given that I open "Calculator Main Page"
  	When I press the "1" button
  	  And I press the "Add" button
  	  And I press the "2" button
  	  And I press the "Equals" button
  	Then I expect that "Result" contains "3"