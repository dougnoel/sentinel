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
    	
  @251B
  Scenario: Attribute Tests
  	Given I am on the Internet Page
  	Then I verify the Header has the attribute class
  	  But I verify the Header does not have the attribute bob
  	  And I verify the Header has the class heading
  	  But I verify the Header does not have the class footer
  
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
 
  @251G
  Scenario: Do Not Find JS Alert On Page
    Given I am on the Javascript Alerts Page
    Then I verify a JS alert is not present 	
  
  @251H
  Scenario: Find JS Alert On Page
    Given I am on the Javascript Alerts Page
      And I click the click for js alert button
    Then I verify a JS alert is present
      And I close the JS alert