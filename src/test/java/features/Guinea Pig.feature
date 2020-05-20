#Author: Doug Noël
@example @saucelabs
Feature: Example Feature
  This is an example of a test using Cucumber.

  Scenario: Guinea Pig
    Given I am on the Guinea Pig Page
    And I verify the URL contains the text "guinea-pig"
    When I click the I am a link Link
  
  @#87  
  Scenario: 87 Add the ability to press keys using a cucumber step
  	Given I am on the Guinea Pig Page
  	When I press the enter key
  		And I press the return key
  		And I press the tab key
  		And I press the escape key