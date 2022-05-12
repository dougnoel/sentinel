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
  	Then I verify a new window opens the EncodeDNA PopUp Window
  		And I verify the Header contains the text "A New Popup Window"
  	When I close the browser window
  	Then I verify the Header contains the text "Demo - Open New Browser Window(s) using JavaScript"
  	
  @228B
  Scenario: 228B Open New Tab
  	Given I am on the Encode DNA Home Page
  	When I click the Open New Tab Button
  	Then I verify a new tab opens to the Encode DNA New Tab Page
  		And I verify the Header contains the text "Window Opened in a New Tab"
  	When I close the browser tab
  	Then I verify the Header contains the text "Demo - Open New Browser Window(s) using JavaScript"