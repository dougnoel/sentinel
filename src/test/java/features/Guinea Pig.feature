#Author: Doug Noël
@example
Feature: Example Feature
  This is an example of a test using Cucumber.

  Scenario: Guinea Pig
    Given I am on the Guinea Pig Page
    And I wait 5.0 seconds
    And I verify the URL contains the text "guinea-pig"
    When I click the I am a link Link
