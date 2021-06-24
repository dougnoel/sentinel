#language: en
#Author: Shobana Namburaj
@201
Feature: 201 Heroku App Drag and Drop
  Login to the HerokuApp 
  And click on the Drag and Drop Example
  And Drag the A Column to B column
  
  @201A  
  Scenario: 201A Successfully Dragged A to B
  	Given I am on the Drag And Drop Page
  	When I drag Box A to Box B
  	Then I verify the Column B Div has the text "A"