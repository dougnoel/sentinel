#Author: Doug Noël
@example @checkbox @216
Feature: 216 Checkbox Tests
  As a developer I want to know that checkbox functionality is working whenever I run unit tests.
	
  @216A
  Scenario: 216A Checkbox Tests
    Given I am on the Internet Checkboxes Page
    When I click on checkbox one
      And I click on checkbox two
        Then I verify checkbox one is checked
      And I verify checkbox two is unchecked
      
  @216B @WindowsOnly
  Scenario: 216B Windows Checkbox Test
  	Moved to a separate test because leaving the context menu up causes tests to fail on the Mac. 
  	Sending an ESCAPE ot RETURN key did not close it. #	 TODO: Fix this to pass on Mac.
    Given I am on the Internet Checkboxes Page
    	And I click on checkbox one
    When I context click on checkbox one
    Then I verify checkbox one is checked