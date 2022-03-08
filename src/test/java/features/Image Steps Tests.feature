#Author: https://github.com/sampacos
@image-steps
Feature: Image Steps Tests
  Testing the Image Steps

  @imagecomparison    
  Scenario: Take And Compare Screenshot
    Given I am on the Javascript Alerts Page
      And I take a screenshot of the click for js alert button
    Then I verify the click for js alert button matches the expected image with 0.00 pixel tolerance level and 1 pixel location threshold
