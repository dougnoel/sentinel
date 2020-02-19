#Author: Doug Noël
@example
Feature: Table Tests
  Testing the Table Page Object Functionality
	
  Scenario: NGX Data Table
    Given I am on the NGX Data Table Page
    Then I verify the Example Table contains the Name column
    	And I see 99 rows in the Example Table
    	
  Scenario: Table
    Given I am on the Table Page
    Then I verify the Example Table contains the Name column
    	And I see 2 rows in the Example Table
    	And I verify the First Name column in the Example Table contains the text Bob
    	And I verify the First Name column in the Example Table does not contain the text Sam
    	And I verify all the cells in the Last Name column in the Example Table contain the text Smith
    	And I verify all the cells in the Last Name column in the Example Table do not contain the text Brown
    	And I verify the cells in the First Name column in the Example Table are sorted in ascending order
    	And I verify the cells in the Last Name column in the Example Table are sorted in descending order