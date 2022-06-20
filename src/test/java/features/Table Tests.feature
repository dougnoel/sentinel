#Author: Doug Noël
@example @table
Feature: Table Tests
  Testing the Table Page Object Functionality
	
  @#44 @ngx-datatable
  Scenario: NGX Data Table
    Given I am on the NGX Data Table Page
    Then I verify the Example Table contains the Name column
    When I find and click the header for the Name column in the Example table
    Then I verify the cells in the Name column in the Example Table are sorted in ascending order
      And I see 100 rows in the Example Table
      And I verify the cell in the 1st row and the Name column of the Example table contains the text Alexander Foley
      And I verify the cell in the 1st row and the Gender column of the Example table contains the text male
      And I verify the cell in the 2nd row and the Name column of the Example table contains the text Alisha Myers
      And I verify the cell in the 2nd row and the Gender column of the Example table contains the text female
    	
  @html-table  	
  Scenario: Table
    Given I am on the Table Page
    Then I verify the Example Table contains the Name column
      And I see 3 rows in the Example Table
      And I verify the First Name column in the Example Table contains the text B
      And I verify the First Name column in the Example Table does not contain the text Sam
      And I verify the First Name column in the Example Table does not have the text B
      And I verify the First Name column in the Example Table has the text Bob
      And I verify all the cells in the Last Name column in the Example Table contain the text Smith
      And I verify all the cells in the Last Name column in the Example Table do not contain the text Brown
   	  And I verify the cells in the First Name column in the Example Table are sorted in ascending order
      And I verify the cells in the Distance column in the Example Table are sorted in ascending order
      And I verify the cells in the ID Number column in the Example Table are sorted in descending order
      And I view the 1st page of results from the Example Table
      And I verify the cell in the 1st row and the First Name column of the Example table contains the text Bob
      And I verify the cell in the 1st row and the First Name column of the Example table does not contain the text Charlotte
      And I verify the cell in the 2nd row and the First Name column of the Example table contains the text Charlotte
      And I verify the cell in the 3rd row and the Zip Code column of the Example table has the text 02111
      And I verify the cell in the 3rd row and the Zip Code column of the Example table does not have the text 0211
      And I verify the cell in the last row and the City column of the Example table contains the text Boston
      And I verify the cell in the last row and the City column of the Example table does not contain the text boston
      And I verify all cells in the City column in the Example table are not empty
      And I verify the First Name column in the Example table is displayed to the left of the Last Name column
    	
  @#44 @link-tests
  Scenario: Testing links inside tables using chaining locators
    Given I am on the NGX Data Table Page
    Then I find the Example Table and click the text female in the row containing the text Ethel Price
  	  And I find the 2nd row in the Example Table and click the text Claudine Neal
      And I find the last row in the Example Table and click the text Humphrey Curtis
    Given I am on the Table Page
    Then I find the Example Table and click the xpath .//button in the row containing the xpath .//*[@id='Dave']/..
      And I verify the JS alert contains the text This is Dave
      And I verify the JS alert does not contain the text This is Charlotte
    When I accept the JS alert
  	  And I find the Example Table and click the xpath .//button in the row containing the text A1356
    Then I verify the JS alert contains the text This is Bob
    When I accept the JS alert
      And I find the Example Table and click the text Alert for Bob in the row containing the xpath .//td[.='Bob']/..
    Then I verify the JS alert contains the text This is Bob
    When I accept the JS alert
  	  And I find the 2nd row in the Example Table and click the text Alert for Charlotte
    Then I verify the JS alert contains the text This is Charlotte
      And I accept the JS alert
    Then I verify all cells in the Empty Column column in the Example Table are empty
