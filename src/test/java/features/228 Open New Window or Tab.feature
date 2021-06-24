#Author: Doug Noel

@228
Feature: 228 Open New Window or Tab.feature
  Open a New Window and Ensure it Opens
  Close the Window and ensure we go back to the previous window
  Open a New Tab and ensure it opens
  Close the tab and ensure we go back to the previous window
  
  @228A
  Scenario: 228A Open New Window
  	Given I am on the Encode DNA Home Page
  	When I click the Open New Window Button
  	Then I verify a new window opens
  		And The Window text exists
  	When I close the window
  	Then I verify the window is closed and we are back on the original page
  	
  @228B
  Scenario: 228B Open New Tab
  	Given I am on the Encode DNA Home Page
  	When I click the Open New Tab Button
  	Then I verify a new tab opens
  		And The Window text exists
  	When I close the tab
  	Then I verify the tab is closed and we are back on the original page