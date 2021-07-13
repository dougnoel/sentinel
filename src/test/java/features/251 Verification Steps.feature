@251
Feature: 251 Verification Steps
Create cucumber tests for all VerificationSteps. Update and rename "Element Exists Feature". Will increase code coverage.

  @251A @145
  Scenario: Test Link Existance
  Validate the Fucntionality of Elements Existing and Not Existing
    Given I am on the Guinea Pig Page
    Then I verify the Example Div exists
    	And I verify the Example Link exists
    	And I verify the Invisible Div does not exist
    	And I verify the Fake Div does not exist