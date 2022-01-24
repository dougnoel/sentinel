#Author: Tyler Bouchard

@2291
Feature: 2291 Switch Between Active Windows and Tabs
  Open a New Tab and Ensures it Opens
  Switch to the Previous Tab and Ensure We Have Switched
  Open a New Window and Ensures it Opens
  Switch to the New Tab and Ensures We Have Switched
  Switch to the New Window and Ensures We Have Switched
  Switch to the Original Window and Ensures We Have Switched
  	
  @2291
  Scenario: 2291 Switch Between Active Tabs and Pages Together
  	Given I am on the Encode DNA Home Page
  	When I click the Open New Tab Button
  	Then I verify a new tab opens to the Encode DNA New Tab Page
  	  And I switch focus to the Encode DNA Home Page
  	When I click the Open New Window Button
  	  And I verify a new window opens the Encode DNA PopUp Window
  	When I switch focus to the Encode DNA New Tab Page
  	Then I verify the Header contains the text "Window Opened in a New Tab"
  	When I switch focus to the Encode DNA PopUp Window
  	Then I verify the Header contains the text "A New Popup Window"
  	When I switch focus to the Encode DNA Home Page
  	Then I verify the Header contains the text "Demo - Open New Browser Window(s) using JavaScript"