package com.dougnoel.sentinel.steps;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.FileException;
import com.dougnoel.sentinel.files.CsvFile;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.system.FileManager;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import static org.junit.Assert.*;


public class CsvSteps {
    private static final Logger log = LogManager.getLogger(CsvSteps.class.getName()); // Create a logger.

    private static final String CONTAIN = "contain";

    /**
     * Sets the most recently-downloaded file to be the current file-under-test with the given number of header rows.
     * <br>
     * <b>Gherkin Examples:</b>
     * <ul>
     *     <li>I open the last downloaded CSV file with 3 header rows</li>
     *     <li>I open the last downloaded csv file with 1 header row</li>
     * </ul>
     *
     * @param numberOfHeaderRows int the number of header rows in the CSV file to test. Number of header rows must be declared in order to properly process the file.
     * @throws FileNotFoundException In the case that the file is not found in the location that the DownloadManager specifies.
     */
    @When("^I find and open the last downloaded (?:CSV|csv) file with (\\d+) header rows?$")
    public static void openMostRecentlyDownloadedFileAsCsv(int numberOfHeaderRows) throws FileNotFoundException {
        FileManager.setCurrentTestFile(new CsvFile(numberOfHeaderRows));
    }

    /**
     * Sets the given file to be the current file-under-test with the given number of header rows.
     * <br>
     * <b>Gherkin Examples:</b>
     * <ul>
     *     <li>I open test file with 1 header as a CSV file with 1 header row</li>
     *     <li>I open src/test/resources/csv/test_0header.csv as a CSV file with 0 header rows</li>
     * </ul>
     *
     * @param fileLocation String either the location of the file, given by a path, or the name of a testdata object in the current page object.
     * @param numberOfHeaderRows int the number of header rows in the CSV file to test. Number of header rows must be declared in order to properly process the file.
     * @throws FileNotFoundException In the case that the file is not found in the location specified.
     */
    @When("^I open (.*) as a (?:CSV|csv) file with (\\d+) header rows?$")
    public static void openSpecificFileAsCsv(String fileLocation, int numberOfHeaderRows) throws FileNotFoundException {
        String filePath;
        try{
            filePath = Configuration.getTestData(fileLocation.trim(), "fileLocation");
        }
        catch(FileException fe){
            filePath = fileLocation;
        }

        FileManager.setCurrentTestFile(new CsvFile(Path.of(filePath), numberOfHeaderRows));
    }

    /**
     * Edits the current CSV file, setting every cell in the given column to the given value.
     * <br>
     * <b>Gherkin Example:</b>
     * <ul>
     *     <li>I set all values in the Name column to Sunny in the CSV file</li>
     * </ul>
     *
     * @param column String column the name of the column, or an ordinal (1st, 2nd, 25th, etc.).
     * @param desiredValue String the value to set each cell to.
     */
    @When("^I set all values in the (.*) column to (.*) in the (?:CSV|csv) file$")
    public static void setAllColumnValuesInCsvFile(String column, String desiredValue){
        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();

        String firstColumnCharacter = column.substring(0, 1);
        if(StringUtils.isNumeric(firstColumnCharacter)){
            file.writeAllCellsInColumn(SentinelStringUtils.parseOrdinal(column), desiredValue);
        }
        else{
            file.writeAllCellsInColumn(column, desiredValue);
        }
    }

    /**
     * Edits the current CSV file, setting the cell in a given column and row to the given value.
     * <br>
     * <b>Gherkin Example:</b>
     * <ul>
     *     <li>I set the value in the <b>last</b> row of the <b>Name</b> column to <b>Sunny</b> in the <b>CSV</b> file</li>
     *     <li>I set the value in the <b>1st</b> row of the <b>Name</b> column to <b>Sunny</b> in the <b>CSV</b> file</li>
     * </ul>
     *
     * @param row String the ordinal of the row to modify (last, 1st, 2nd, 25th, etc.).
     * @param column String the name of the column to modify, or an ordinal (1st, 2nd, 25th, etc.).
     * @param desiredValue String the value to set the cell to.
     */
    @When("^I set the value in the (la|\\d+)(?:st|nd|rd|th) row of the (.*) column to (.*) in the (?:CSV|csv) file$")
    public static void setRowColumnValuesInCsvFile(String row, String column, String desiredValue){
        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();

        String firstColumnCharacter = column.substring(0, 1);
        int rowIndex = row.equals("la") ? file.getNumberOfDataRows() : Integer.parseInt(row);
        if(StringUtils.isNumeric(firstColumnCharacter)){
            file.writeCellInColumnRow(SentinelStringUtils.parseOrdinal(column), rowIndex, desiredValue);
        }
        else{
            file.writeCellInColumnRow(column, rowIndex, desiredValue);
        }
    }

    /**
     * Edits the current CSV file and completely removes the given row.
     * <br>
     * <b>Gherkin Example:</b>
     * <ul>
     *     <li>I delete the <b>last</b> row entry in the <b>CSV</b> file</li>
     *     <li>I delete the <b>2nd row</b> entry in the <b>CSV</b> file</li>
     * </ul>
     *
     * @param row String the ordinal of the row to delete (last, 1st, 2nd, 25th, etc.).
     */
    @When("^I delete the (la|\\d+)(?:st|nd|rd|th) row entry in the (?:CSV|csv) file$")
    public static void deleteCsvRow(String row){
        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();

        int rowIndex = row.equals("la") ? file.getNumberOfDataRows() : Integer.parseInt(row);
        file.deleteRow(rowIndex);
    }

    /**
     * Verifies the current CSV file has or does not have the given text in the given column and given row.
     *
     * <br>
     * <b>Gherkin Examples:</b>
     * <ul>
     *     <li>I verify the csv file has the value Sonny in the Name column and the 3rd row</li>
     *     <li>I verify the CSV file does not contain the value Liston in the Surname column and the last row</li>
     * </ul>
     *
     * @param assertion String if null is passed, looks for match(es), if any strong value is passed, looks for the value to not exist.
     * @param matchType String whether we are doing an exact match or a partial match.
     * @param textToMatch String the text to look for in the cell.
     * @param column String column the name of the column, or an ordinal (1st, 2nd, 25th, etc.).
     * @param rowNum String the row number. Can be "la" to specify the last row, or an integer.
     */
    @Then("^I verify the (?:CSV|csv) file( do(?:es)? not)? (has|have|contains?) the value (.*) in the (.*) column and the (la|\\d+)(?:st|nd|rd|th) row$")
    public static void verifyCsvCellHasValue(String assertion, String matchType, String textToMatch, String column, String rowNum) {
        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();
        boolean negate = !StringUtils.isEmpty(assertion);
        int rowIndex = rowNum.equals("la") ? file.getNumberOfDataRows() : Integer.parseInt(rowNum);
        boolean partialMatch = matchType.contains(CONTAIN);

        var expectedResult = SentinelStringUtils.format(
                "Expected the cell in the {} row and the {} column of the CSV file to {}contain the text {}.",
                SentinelStringUtils.ordinal(rowIndex), column, (negate ? "not " : ""), textToMatch);
        log.trace(expectedResult);

        String firstColumnCharacter = column.substring(0, 1);
        if(StringUtils.isNumeric(firstColumnCharacter)){
            if (negate) {
                assertNotNull(expectedResult, file.verifyCellDataContains(rowIndex, SentinelStringUtils.parseOrdinal(column), textToMatch, partialMatch));
            } else {
                assertNull(expectedResult, file.verifyCellDataContains(rowIndex, SentinelStringUtils.parseOrdinal(column), textToMatch, partialMatch));
            }
        }
        else{
            if (negate) {
                assertNotNull(expectedResult, file.verifyCellDataContains(rowIndex, column, textToMatch, partialMatch));
            } else {
                assertNull(expectedResult, file.verifyCellDataContains(rowIndex, column, textToMatch, partialMatch));
            }
        }
    }

    /**
     * Verifies all / not all cells in the given column of the current CSV file have / contain the given text value.
     * <br>
     * <b>Gherkin Examples:</b>
     * <ul>
     *     <li>I verify all cells in the Name column in the csv file have the value Sonny</li>
     *     <li>I verify not all cells in the Surname column in the csv file contain the value List</li>
     * </ul>
     *
     * @param column String column the name of the column, or an ordinal (1st, 2nd, 25th, etc.).
     * @param assertion String if null is passed, looks for all cells to have/contain the value. If " not", looks for at least one cell to NOT have/contain the value.
     * @param matchType String whether we are doing an exact match or a partial match.
     * @param textToMatch String the text to look for in the cell.
     */
    @Then("^I verify( not)? all cells in the the (.*) column (?:of|in) the (?:CSV|csv) file (has|have|contains?) the value (.*)$")
    public static void verifyCsvAllColumnCellsHaveValue(String assertion, String column, String matchType, String textToMatch){
        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();
        boolean negate = !StringUtils.isEmpty(assertion);
        boolean partialMatch = matchType.contains(CONTAIN);

        var expectedResult = SentinelStringUtils.format(
                "Expected all cells in the {} column of the CSV file to {}contain the text {}.",
                            column, (negate ? "not " : ""), textToMatch);
        log.trace(expectedResult);

        String firstColumnCharacter = column.substring(0, 1);
        if(StringUtils.isNumeric(firstColumnCharacter)){
            if (negate) {
                assertFalse(expectedResult, file.verifyAllColumnCellsContain(SentinelStringUtils.parseOrdinal(column), textToMatch, partialMatch));
            } else {
                assertTrue(expectedResult, file.verifyAllColumnCellsContain(SentinelStringUtils.parseOrdinal(column), textToMatch, partialMatch));
            }
        }
        else{
            if (negate) {
                assertFalse(expectedResult, file.verifyAllColumnCellsContain(column, textToMatch, partialMatch));
            } else {
                assertTrue(expectedResult, file.verifyAllColumnCellsContain(column, textToMatch, partialMatch));
            }
        }
    }

    /**
     * Verifies all cells in the csv column are or are not empty. If checking that all column cells are empty, this method will assert that every cell is empty.
     * If checking that all column cells are not empty, this method will assert that every cell is NOT empty (all cells have at least some content).
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify all cells are empty in the Zip Code column in the csv file</li>
     * <li>I verify all cells are not empty in the Name column of the CSV file</li>
     * </ul>
     *
     * @param assertion String if null, checks that all cells are empty. Otherwise, checks that all cells are not empty.
     * @param column String name of the column in the csv, or an ordinal column index (1st, 2nd, 3rd, etc.)
     */
    @Then("^I verify all cells are (not )?empty in the (.*) column (?:of|in) the (?:CSV|csv) file$")
    public static void verifyCsvAllColumnCellsAreEmpty(String assertion, String column){
        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();
        boolean negate = !StringUtils.isEmpty(assertion);

        var expectedResult = SentinelStringUtils.format(
                "Expected all cells in the {} column of the CSV file to {}be empty.",
                column, (negate ? "not " : ""));
        log.trace(expectedResult);

        String firstColumnCharacter = column.substring(0, 1);
        if(StringUtils.isNumeric(firstColumnCharacter)){
            if (negate) {
                assertTrue(expectedResult, file.verifyAllColumnCellsNotEmpty(SentinelStringUtils.parseOrdinal(column)));
            } else {
                assertTrue(expectedResult, file.verifyAllColumnCellsEmpty(SentinelStringUtils.parseOrdinal(column)));
            }
        }
        else{
            if (negate) {
                assertTrue(expectedResult, file.verifyAllColumnCellsNotEmpty(column));
            } else {
                assertTrue(expectedResult, file.verifyAllColumnCellsEmpty(column));
            }
        }
    }

    /**
     * Verifies the given table contains or exactly matches or does not contain or does not exactly match the given column
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the csv contains the Phone Number column</li>
     * <li>I verify the CSV does not contain a Deductible column</li>
     * </ul>
     * @param assertion String if null is passed, looks for match(es), if any strong value is passed, looks for the value to not exist.
     * @param matchType String whether we are doing an exact match or a partial match
     * @param columnName String name of the column to verify
     */
    public static void verifyCsvColumnExists(String assertion, String matchType, String columnName) {
        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();
        boolean negate = !StringUtils.isEmpty(assertion);
        String negateText = negate ? "not " : "";
        boolean partialMatch = matchType.contains(CONTAIN);
        String partialMatchText = partialMatch ? CONTAIN : "exactly match";

        String expectedResult = SentinelStringUtils.format("Expected the {} column to {}{} the column header in the CSV.",
                columnName, negateText, partialMatchText);
        if (negate) {
            assertFalse(expectedResult, file.verifyColumnHeaderEquals(columnName, partialMatch));
        } else {
            assertTrue(expectedResult, file.verifyColumnHeaderEquals(columnName, partialMatch));
        }
    }


    /**
     * Verifies a csv column does or does not contain text for the stored value.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the Name column of the csv contains the same text entered for the username</li>
     * <li>I verify the Contact column of the csv contains the same text used for the phone number field</li>
     * <li>I verify the Airport Code column of the CSV contains the same text selected for the airport's code RDU</li>
     * <li>I verify the Airport Code column of the CSV does not contain the same text selected for the airport's code RDU</li>
     * </ul>
     * @param column String Name of the column to verify
     * @param assertion String Assertion dictating if we are checking if the column does contain or does not contain
     * @param key String the key to retrieve the text to match from the configuration manager
     */
    @Then("^I verify the (.*?) column of the (?:csv|CSV)( do(?:es)? not)? (has|have|contains?) the same text (?:entered|selected|used) for the (.*)$")
    public static void verifyStoredTextAppearsInColumn(String column, String assertion, String matchType, String key) {
        var textToMatch = Configuration.toString(key);
        String errorMessage = SentinelStringUtils.format("No previously stored text was found for the \"{}\" key.", key);
        Assert.assertNotNull(errorMessage, textToMatch);

        verifyTextAppearsInColumn(column, assertion, matchType, textToMatch);
    }

    /**
     * Verifies a csv column does or does not contain text. Can be partial or exact match.
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the Name column of the csv contains the text Sam</li>
     * <li>I verify the Timezone column of the csv has the text EST </li>
     * <li>I verify the Airport Code column of the CSV does not contain the text RDU</li>
     * <li>I verify the Airport Code column of the CSV does not have the text RDU</li>
     * </ul>
     * @param column String Name of the column to verify
     * @param assertion String Assertion dictating if we are checking if the column does contain or does not contain
     * @param textToMatch String the key to retrieve the text to match from the configuration manager
     */
    @Then("^I verify the (.*?) column of the (?:csv|CSV)( do(?:es)? not)? (has|have|contains?) the text (.*)$")
    public static void verifyTextAppearsInColumn(String column, String assertion, String matchType, String textToMatch) {
        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();
        boolean negate = !StringUtils.isEmpty(assertion);
        String errorMessage;
        boolean partialMatch = matchType.contains(CONTAIN);

        errorMessage = SentinelStringUtils.format("Expected the {} column of the CSV to contain any cells with the text {}", column, textToMatch);
        String firstColumnCharacter = column.substring(0, 1);
        if(StringUtils.isNumeric(firstColumnCharacter)){
            if (negate) {
                assertFalse(errorMessage, file.verifyAnyColumnCellContains(SentinelStringUtils.parseOrdinal(column), textToMatch, partialMatch));
            } else {
                assertTrue(errorMessage, file.verifyAnyColumnCellContains(SentinelStringUtils.parseOrdinal(column), textToMatch, partialMatch));
            }
        }
        else{
            if (negate) {
                assertFalse(errorMessage, file.verifyAnyColumnCellContains(column, textToMatch, partialMatch));
            } else {
                assertTrue(errorMessage, file.verifyAnyColumnCellContains(column, textToMatch, partialMatch));
            }
        }
    }

    /**
     * Verifies the CSV has the given number of rows of data, not including the header rows.
     *  <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I verify the csv has 1 data row</li>
     * <li>I verify the CSV has 22 data rows</li>
     * </ul>
     * @param numRows int the number of rows to verify against.
     */
    @Then("^I verify the (?:CSV|csv) has (\\d+) data rows?")
    public static void verifyNumberOfRows(int numRows){
        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();
        String errorMessage = SentinelStringUtils.format("Expected the CSV to have {} rows, not including headers.", numRows);
        assertEquals(errorMessage, numRows, file.getNumberOfDataRows());
    }

    /**
     * Stores the value of a specific row and column cell within a CSV file with a given name.
     * Used for storing a CSV cell value that will not be modified for later comparison.
     *  <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I store the cell value in the <b>last</b> row of the <b>name</b> column in the csv as <b>last name entry</b></li>
     * <li>I store the cell value in the <b>4th</b> row of the <b>1st</b> column in the csv as <b>comment entry</b></li>
     * </ul>
     * @param rowNum String the row of the cell. Can contain an ordinal such as 1st, 3rd, 4th, or last
     * @param column String the column of the cell. Can contain the column name or it's ordinal form
     * @param storageKey String the name to store the value as
     */
    @Then("^I store the cell value in the (la|\\d+)(?:st|nd|rd|th) row of the (.*?) column in the (?:csv|CSV) as (.*?)$")
    public static void storeCsvValue(String rowNum, String column, String storageKey) {
        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();
        int rowIndex = rowNum.equals("la") ? file.getNumberOfDataRows() : Integer.parseInt(rowNum);

        String storageValue;
        String firstColumnCharacter = column.substring(0, 1);
        if(StringUtils.isNumeric(firstColumnCharacter)){
            storageValue = file.readCellData(SentinelStringUtils.parseOrdinal(column), rowIndex);
        }
        else{
            storageValue = file.readCellData(column, rowIndex);
        }

        Configuration.update(storageKey, storageValue);
    }
}