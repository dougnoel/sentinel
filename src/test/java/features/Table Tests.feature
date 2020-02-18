#Author: Doug Noël

Feature: Table Tests
  Testing the Table Page Object Functionality
	@example
  Scenario: NGX Data Table
    Given I am on the NGX Data Table Page
    Then I verify the Example Table contains the Name column
    	And I see 99 rows in the Example Table
    	
  Scenario: Table
    Given I am on the Table Page
    Then I verify the Example Table contains the Name column
    	And I see 2 rows in the Example Table
