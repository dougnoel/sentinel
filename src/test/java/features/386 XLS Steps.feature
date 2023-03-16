#author: peterkrzywicki

@386 @xls
Feature: Verify and Edit XLSs

  @386B @xls
  Scenario: Edit all values in XLS file column
    Given I navigate to the Textbox Page
      And I enter Truman in the last name field
      And I navigate to the Upload Page
    When I open src/test/resources/xls/test_1header.xlsx as a Xls file with 2 header rows
    Then I open src/test/resources/xls/test_1header_excel97.xls as a Xls file with 1 header rows
#      And I set all values in the surname column to Truman in the CSV file
#    Then I verify the CSV file has the value Truman in the surname column and the 1st row
#      And I verify the CSV file contains the value Truman in the surname column and the 4th row
#      And I verify the CSV file does not have the value Truman in the surname column and the 0th row
#      And I verify the CSV file does not contain the value Truman in the surname column and the 0th row
#      And I verify the surname column of the csv contains the same text entered for the last name field
#      And I verify the 3rd column of the csv contains the same text entered for the last name field
#      And I verify the 1st column of the csv does not contain the same text entered for the last name field
#      And I verify the 4th column of the csv does not contain the same text entered for the last name field
#      And I verify all cells in the the surname column of the csv file contain the value Trum
#      And I verify not all cells in the the surname column of the csv file contain the value William
#      And I verify the csv has 4 data row
#      And I verify the csv contains the comm column
#      And I verify the csv has the comment column
#      And I verify the csv does not have the nonexistant column
#      And I verify all cells are not empty in the surname column in the csv file
#      And I verify all cells are empty in the comment column in the csv file
#      And I verify all cells are not empty in the 4th column in the csv file
#      And I verify all cells are empty in the 5th column in the csv file
#      And I verify the surname column of the csv has the text Truman
#      And I verify the surname column of the csv does not have the text truman
#      And I verify the 3rd column of the csv contains the text Tru
#      And I verify the 3rd column of the csv does not contain the text tru
#    When I open src/test/resources/csv/test_0header.csv as a CSV file with 0 header rows
#      And I set all values in the 4th column to 6 in the CSV file
#    Then I verify the CSV file has the value 6 in the 4th column and the 1st row
#      And I verify the CSV file has the value 6 in the 4th column and the 4th row
#      And I verify all cells in the the 4th column of the csv file contain the value 6
#      And I verify not all cells in the the 4th column of the csv file contain the value 1
#    When I open src/test/resources/csv/test_1header.csv as a CSV file with 1 header row
#      And I set all values in the surname column to Williams in the CSV file
#    Then I verify the CSV file has the value Williams in the surname column and the 1st row
#      And I verify the CSV file has the value Williams in the surname column and the 4th row
#      And I verify all cells in the the surname column of the csv file have the value Williams
#      And I verify not all cells in the the surname column of the csv file have the value Truman
#    When I open src/test/resources/csv/test_0header.csv as a CSV file with 0 header rows
#      And I set all values in the 4th column to 12345 in the CSV file
#    Then I verify the CSV file has the value 12345 in the 4th column and the 1st row
#      And I verify the CSV file has the value 12345 in the 4th column and the 4th row
#      And I verify all cells in the the 4th column of the csv file have the value 12345
#      And I verify not all cells in the the 4th column of the csv file have the value 6
