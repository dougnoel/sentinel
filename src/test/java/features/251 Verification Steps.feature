@251
Feature: 251 Verification Steps
Create cucumber tests for all VerificationSteps. Update and rename "Element Exists Feature". Will increase code coverage.

  @251A @145
  Scenario: Test Link Existence
  Validate the Fucntionality of Elements Existing and Not Existing
    Given I am on the Guinea Pig Page
    Then I verify the Example Div exists
    	And I verify the Example Link exists
    	And I verify the Invisible Div does not exist
    	And I verify the Fake Div does not exist
    	
#  @251B
#  Scenario: Has Attribute and Does Not Have Attribute
#  
#  @251C
#  Scenario: Is Active and Is Not Active
#  
#  @251D
#  Scenario: Is Enabled and Is Not Enabled
#    
#  @251E
#  Scenario: Is Hidden and Is Not Hidden
  
  @251F
  Scenario: Switch to iFrame
  Validate you can switch to an iFrame and back out.
  	Given I am on the Internet IFrame Page
  	When I enter the iFrame
  	Then I exit the iFrame