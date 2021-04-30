#Author: Doug Noël
@base-steps
Feature: Base Steps Tests
  Testing the Base Steps
	
  @#204 @wait
  Scenario: Wait
    Given I wait 0.001 seconds
    	And I wait 0.001 second more