#language: en
#Author: Shobana Namburaj
@201
Feature: 201 Heroku App Drag and Drop
  Login to the HerokuApp 
  And click on the Drag and Drop Example
  And Drag the A Column to B column
  
  @201A  
  Scenario: 201A Successfully Dragged A to B
  	Given I am on the Internet Herokuapp Page
  	When I click a drag and drop link  
  	And I wait 2 seconds	 
  	Then I am redirected to the Drag And Drop Page
  	When I drag and drop from source element to target element