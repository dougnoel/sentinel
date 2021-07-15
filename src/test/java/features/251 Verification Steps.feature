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
  @251D
  Scenario: Enabled and Disabled Tests
  	Given I am on the Internet Dynamic Controls Page
  	When I click the Enable Button
  	Then I verify the Input Textbox is enabled
  	When I click the Disable Button
  	Then I verify the Input Textbox is disabled

    
  @251E
  Scenario: Element Visibility Tests
  	Given I am on the Guinea Pig Page
  	Then I verify the hidden div is hidden
  	  And I verify the your comments span is visible
  
  @251F
  Scenario: Switch to iFrame
  Validate you can switch to an iFrame and back out.
  	Given I am on the Internet IFrame Page
  	When I enter the iFrame
  	Then I exit the iFrame