#Author: Doug Noël
@base-steps
Feature: Base Steps Tests
  Testing the Base Steps
	
  @#204 @wait
  Scenario: Wait
    Given I wait 0.001 seconds
    	And I wait 0.001 second more
    	
  Scenario: Accept JS Alert
    Given I am on the Javascript Alerts Page
      And I click the click for js alert button
      And I accept the JS alert
    Then I verify the result message has the text "You successfully clicked an alert"