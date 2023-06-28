#author: sampacos

@386 @csv @525 @555
Feature: Verify and Edit CSVs

  @386A @csv
  Scenario: Edit all values in CSV file column
    Given I navigate to the Textbox Page
      And I enter Truman in the last name field
      And I navigate to the Upload Page
    When I open csv file with 1 header row as a CSV file with 1 header row
      And I set all values in the surname column to Truman in the CSV file
    Then I verify the CSV file has the value Truman in the surname column and the 1st row
      And I verify the CSV file contains the value Truman in the surname column and the 4th row
      And I verify the CSV file does not have the value Truman in the surname column and the 0th row
      And I verify the CSV file does not contain the value Truman in the surname column and the 0th row
      And I verify the surname column of the csv contains the same text entered for the last name field
      And I verify the 3rd column of the csv contains the same text entered for the last name field
      And I verify the 1st column of the csv does not contain the same text entered for the last name field
      And I verify the 4th column of the csv does not contain the same text entered for the last name field
      And I verify all cells in the the surname column of the csv file contain the value Trum
      And I verify not all cells in the the surname column of the csv file contain the value William
      And I verify the csv has 4 data row
      And I verify the csv contains the comm column
      And I verify the csv has the comment column
      And I verify the csv does not have the nonexistant column
      And I verify all cells are not empty in the surname column in the csv file
      And I verify all cells are empty in the comment column in the csv file
      And I verify all cells are not empty in the 4th column in the csv file
      And I verify all cells are empty in the 5th column in the csv file
      And I verify the surname column of the csv has the text Truman
      And I verify the surname column of the csv does not have the text truman
      And I verify the 3rd column of the csv contains the text Tru
      And I verify the 3rd column of the csv does not contain the text tru
    When I open src/test/resources/csv/test_0header.csv as a CSV file with 0 header rows
      And I set all values in the 4th column to 6 in the CSV file
    Then I verify the CSV file has the value 6 in the 4th column and the 1st row
      And I verify the CSV file has the value 6 in the 4th column and the 4th row
      And I verify all cells in the the 4th column of the csv file contain the value 6
      And I verify not all cells in the the 4th column of the csv file contain the value 1
    When I open src/test/resources/csv/test_1header.csv as a CSV file with 1 header row
      And I set all values in the surname column to Williams in the CSV file
    Then I verify the CSV file has the value Williams in the surname column and the 1st row
      And I verify the CSV file has the value Williams in the surname column and the 4th row
      And I verify all cells in the the surname column of the csv file have the value Williams
      And I verify not all cells in the the surname column of the csv file have the value Truman
    When I open src/test/resources/csv/test_0header.csv as a CSV file with 0 header rows
      And I set all values in the 4th column to 12345 in the CSV file
    Then I verify the CSV file has the value 12345 in the 4th column and the 1st row
      And I verify the CSV file has the value 12345 in the 4th column and the 4th row
      And I verify all cells in the the 4th column of the csv file have the value 12345
      And I verify not all cells in the the 4th column of the csv file have the value 6

  @525A @csv
  Scenario: Edit specific row column values in a CSV file
    Given I navigate to the Downloads Test Page
    When I verify that by clicking the csv download link a new file is downloaded with the extension csv
      And I find and open the last downloaded csv file with 1 header row
      And I set all values in the comment column to test in the CSV file
      And I set the value in the 2nd row of the comment column to me in the CSV file
      And I set the value in the last row of the 5th column to now in the CSV file
    Then I verify the CSV file has the value me in the comment column and the 2nd row
      And I verify the CSV file has the value now in the comment column and the last row
      And I verify the CSV file has the value test in the comment column and the 1st row

  @525B @csv
  Scenario: Delete a row from a CSV file
    Given I navigate to the Downloads Test Page
    When I verify that by clicking the csv download link a new file is downloaded with the extension csv
      And I find and open the last downloaded csv file with 1 header row
      And I set all values in the comment column to test in the CSV file
      And I set the value in the 2nd row of the comment column to me in the CSV file
      And I set the value in the last row of the 5th column to now in the CSV file
    Then I verify the CSV file has the value me in the comment column and the 2nd row
      And I verify the CSV file has the value now in the comment column and the last row
      And I verify the CSV file has the value test in the comment column and the 1st row
      And I verify the CSV has 4 data rows
    When I delete the last row entry in the CSV file
      And I delete the 2nd row entry in the CSV file
    Then I verify the CSV has 2 data rows
      And I verify the comment column of the csv does not have the text me
      And I verify the 5th column of the csv does not have the text now

  @555A @csv
  Scenario: Store and verify the value of a CSV file
    Given I navigate to the Downloads Test Page
    When I verify that by clicking the csv download link a new file is downloaded with the extension csv
      And I find and open the last downloaded csv file with 1 header row
      And I store the cell value in the last row of the name column in the csv as originalFinalName
      And I store the cell value in the 1st row of the name column in the csv as originalFirstName
    Then I verify the name column of the csv contains the same text used for the originalFinalName
      And I verify the name column of the csv does not contain the text change original final name
      And I verify the name column of the csv does not contain the text change original first name
    When I set the value in the last row of the name column to change original final name in the CSV file
      And I set the value in the 1st row of the name column to change original first name in the CSV file
    Then I verify the name column of the csv contains the text change original final name
      And I verify the name column of the csv contains the text change original first name
      And I verify the name column of the csv does not contain the same text used for the originalFinalName