package com.dougnoel.sentinel.steps;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.exceptions.FileException;
import com.dougnoel.sentinel.files.XlsFile;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.system.FileManager;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;


public class XlsSteps {
    private static final Logger log = LogManager.getLogger(XlsSteps.class.getName()); // Create a logger.

    private static final String CONTAIN = "contain";

    /**
     * Sets the most recently-downloaded file to be the current file-under-test with the given number of header rows.
     * <br>
     * <b>Gherkin Examples:</b>
     * <ul>
     *     <li>I open the last downloaded XLS file with 3 header rows</li>
     *     <li>I open the last downloaded XLS file with 1 header row</li>
     * </ul>
     *
     * @param numHeaderRows int the number of header rows in the XLS file to test. Number of header rows must be declared in order to properly process the file.
     * @throws FileNotFoundException In the case that the file is not found in the location that the DownloadManager specifies.
     */
    @When("^I find and open the last downloaded (?:XLS|xls) file with (\\d+) header rows?$")
    public static void openMostRecentlyDownloadedFileAsXls(int numHeaderRows) throws IOException {
        FileManager.setCurrentTestFile(new XlsFile(numHeaderRows));
    }

    /**
     * Sets the given file to be the current file-under-test with the given number of header rows.
     * <br>
     * <b>Gherkin Examples:</b>
     * <ul>
     *     <li>I open test file with 1 header as a XLS file with 1 header row</li>
     *     <li>I open src/test/resources/xls/test_1header.xls as a CSV file with 0 header rows</li>
     * </ul>
     *
     * @param fileLocation String either the location of the file, given by a path, or the name of a testdata object in the current page object.
     * @param numHeaderRows int the number of header rows in the XLS file to test. Number of header rows must be declared in order to properly process the file.
     * @throws FileNotFoundException In the case that the file is not found in the location specified.
     */
    @When("^I open (.*) as a (?:Xls|xls) file with (\\d+) header rows?$")
    public static void openSpecificFileAsXls(String fileLocation, int numHeaderRows) throws IOException {
        String filePath;
        try{
            filePath = Configuration.getTestData(fileLocation.trim(), "fileLocation");
        }
        catch(FileException fe){
            filePath = fileLocation;
        }

        FileManager.setCurrentTestFile(new XlsFile(Path.of(filePath), numHeaderRows));
    }

//    /**
//     * Edits the current XLS file, setting every cell in the given column to the given value.
//     * <br>
//     * <b>Gherkin Example:</b>
//     * <ul>
//     *     <li>I set all values in the Title column to Sunny in the XLS file</li>
//     * </ul>
//     *
//     * @param column String column the name of the column, or an ordinal (1st, 2nd, 25th, etc.).
//     * @param desiredValue String the value to set each cell to.
//     */
//    @When("^I set all values in the (.*) column to (.*) in the (?:XLS|xls) file$")
//    public static void setAllColumnValuesInXlsFile(String column, String desiredValue){
//        XlsFile file = (XlsFile) FileManager.getCurrentTestFile();
//
//        String firstColumnCharacter = column.substring(0, 1);
//        if(StringUtils.isNumeric(firstColumnCharacter)){
//            file.writeAllCellsInXlsColumn(SentinelStringUtils.parseOrdinal(column), desiredValue);
//        }
//        else{
//            file.writeAllCellsInXlsColumn(column, desiredValue);
//        }
//    }
//
    /**
     * Verifies the current XLS file has or does not have the given text in the given column and given row.
     *
     * <br>
     * <b>Gherkin Examples:</b>
     * <ul>
     *     <li>I verify the xls file has the value Sonny in the Name column and the 3rd row</li>
     *     <li>I verify the XLS file does not contain the value Liston in the Surname column and the last row</li>
     * </ul>
     *
     * @param assertion String if null is passed, looks for match(es), if any strong value is passed, looks for the value to not exist.
     * @param matchType String whether we are doing an exact match or a partial match.
     * @param textToMatch String the text to look for in the cell.
     * @param column String column the name of the column, or an ordinal (1st, 2nd, 25th, etc.).
     * @param rowNum String the row number. Can be "la" to specify the last row, or an integer.
     */
    @Then("^I verify the (?:XLS|xls) file( do(?:es)? not)? (has|have|contains?) the value (.*) in the (.*) column and the (la|\\d+)(?:st|nd|rd|th) row$")
    public static void verifyXlsCellHasValue(String assertion, String matchType, String textToMatch, String column, String rowNum) {
        XlsFile file = (XlsFile) FileManager.getCurrentTestFile();
        boolean negate = !StringUtils.isEmpty(assertion);
        int rowIndex = rowNum.equals("la") ? file.getNumberOfDataRows() : Integer.parseInt(rowNum);
        boolean partialMatch = matchType.contains(CONTAIN);

        var expectedResult = SentinelStringUtils.format(
                "Expected the cell in the {} row and the {} column of the XLS file to {}contain the text {}.",
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
//
//    /**
//     * Verifies all / not all cells in the given column of the current CSV file have / contain the given text value.
//     * <br>
//     * <b>Gherkin Examples:</b>
//     * <ul>
//     *     <li>I verify all cells in the Name column in the csv file have the value Sonny</li>
//     *     <li>I verify not all cells in the Surname column in the csv file contain the value List</li>
//     * </ul>
//     *
//     * @param column String column the name of the column, or an ordinal (1st, 2nd, 25th, etc.).
//     * @param assertion String if null is passed, looks for all cells to have/contain the value. If " not", looks for at least one cell to NOT have/contain the value.
//     * @param matchType String whether we are doing an exact match or a partial match.
//     * @param textToMatch String the text to look for in the cell.
//     */
//    @Then("^I verify( not)? all cells in the the (.*) column (?:of|in) the (?:XLS|xls) file (has|have|contains?) the value (.*)$")
//    public static void verifyXlsAllColumnCellsHaveValue(String assertion, String column, String matchType, String textToMatch){
//        XlsFile file = (XlsFile) FileManager.getCurrentTestFile();
//        boolean negate = !StringUtils.isEmpty(assertion);
//        boolean partialMatch = matchType.contains(CONTAIN);
//
//        var expectedResult = SentinelStringUtils.format(
//                "Expected all cells in the {} column of the CSV file to {}contain the text {}.",
//                            column, (negate ? "not " : ""), textToMatch);
//        log.trace(expectedResult);
//
//        String firstColumnCharacter = column.substring(0, 1);
//        if(StringUtils.isNumeric(firstColumnCharacter)){
//            if (negate) {
//                assertFalse(expectedResult, file.verifyAllColumnCellsContain(SentinelStringUtils.parseOrdinal(column), textToMatch, partialMatch));
//            } else {
//                assertTrue(expectedResult, file.verifyAllColumnCellsContain(SentinelStringUtils.parseOrdinal(column), textToMatch, partialMatch));
//            }
//        }
//        else{
//            if (negate) {
//                assertFalse(expectedResult, file.verifyAllColumnCellsContain(column, textToMatch, partialMatch));
//            } else {
//                assertTrue(expectedResult, file.verifyAllColumnCellsContain(column, textToMatch, partialMatch));
//            }
//        }
//    }

//    /**
//     * Verifies all cells in the csv column are or are not empty. If checking that all column cells are empty, this method will assert that every cell is empty.
//     * If checking that all column cells are not empty, this method will assert that every cell is NOT empty (all cells have at least some content).
//     * <p>
//     * <b>Gherkin Examples:</b>
//     * <ul>
//     * <li>I verify all cells are empty in the Zip Code column in the csv file</li>
//     * <li>I verify all cells are not empty in the Name column of the CSV file</li>
//     * </ul>
//     *
//     * @param assertion String if null, checks that all cells are empty. Otherwise, checks that all cells are not empty.
//     * @param column String name of the column in the csv, or an ordinal column index (1st, 2nd, 3rd, etc.)
//     */
//    @Then("^I verify all cells are (not )?empty in the (.*) column (?:of|in) the (?:CSV|csv) file$")
//    public static void verifyCsvAllColumnCellsAreEmpty(String assertion, String column){
//        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();
//        boolean negate = !StringUtils.isEmpty(assertion);
//
//        var expectedResult = SentinelStringUtils.format(
//                "Expected all cells in the {} column of the CSV file to {}be empty.",
//                column, (negate ? "not " : ""));
//        log.trace(expectedResult);
//
//        String firstColumnCharacter = column.substring(0, 1);
//        if(StringUtils.isNumeric(firstColumnCharacter)){
//            if (negate) {
//                assertTrue(expectedResult, file.verifyAllColumnCellsNotEmpty(SentinelStringUtils.parseOrdinal(column)));
//            } else {
//                assertTrue(expectedResult, file.verifyAllColumnCellsEmpty(SentinelStringUtils.parseOrdinal(column)));
//            }
//        }
//        else{
//            if (negate) {
//                assertTrue(expectedResult, file.verifyAllColumnCellsNotEmpty(column));
//            } else {
//                assertTrue(expectedResult, file.verifyAllColumnCellsEmpty(column));
//            }
//        }
//    }
//
//    /**
//     * Verifies the given table contains or exactly matches or does not contain or does not exactly match the given column
//     * <p>
//     * <b>Gherkin Examples:</b>
//     * <ul>
//     * <li>I verify the csv contains the Phone Number column</li>
//     * <li>I verify the CSV does not contain a Deductible column</li>
//     * </ul>
//     * @param assertion String if null is passed, looks for match(es), if any strong value is passed, looks for the value to not exist.
//     * @param matchType String whether we are doing an exact match or a partial match
//     * @param columnName String name of the column to verify
//     */
//    public static void verifyCsvColumnExists(String assertion, String matchType, String columnName) {
//        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();
//        boolean negate = !StringUtils.isEmpty(assertion);
//        String negateText = negate ? "not " : "";
//        boolean partialMatch = matchType.contains(CONTAIN);
//        String partialMatchText = partialMatch ? CONTAIN : "exactly match";
//
//        String expectedResult = SentinelStringUtils.format("Expected the {} column to {}{} the column header in the CSV.",
//                columnName, negateText, partialMatchText);
//        if (negate) {
//            assertFalse(expectedResult, file.verifyColumnHeaderEquals(columnName, partialMatch));
//        } else {
//            assertTrue(expectedResult, file.verifyColumnHeaderEquals(columnName, partialMatch));
//        }
//    }
//
//
//    /**
//     * Verifies a csv column does or does not contain text for the stored value.
//     * <p>
//     * <b>Gherkin Examples:</b>
//     * <ul>
//     * <li>I verify the Name column of the csv contains the same text entered for the username</li>
//     * <li>I verify the Contact column of the csv contains the same text used for the phone number field</li>
//     * <li>I verify the Airport Code column of the CSV contains the same text selected for the airport's code RDU</li>
//     * <li>I verify the Airport Code column of the CSV does not contain the same text selected for the airport's code RDU</li>
//     * </ul>
//     * @param column String Name of the column to verify
//     * @param assertion String Assertion dictating if we are checking if the column does contain or does not contain
//     * @param key String the key to retrieve the text to match from the configuration manager
//     */
//    @Then("^I verify the (.*?) column of the (?:csv|CSV)( do(?:es)? not)? (has|have|contains?) the same text (?:entered|selected|used) for the (.*)$")
//    public static void verifyStoredTextAppearsInColumn(String column, String assertion, String matchType, String key) {
//        var textToMatch = Configuration.toString(key);
//        String errorMessage = SentinelStringUtils.format("No previously stored text was found for the \"{}\" key.", key);
//        Assert.assertNotNull(errorMessage, textToMatch);
//
//        verifyTextAppearsInColumn(column, assertion, matchType, textToMatch);
//    }
//
//    /**
//     * Verifies a csv column does or does not contain text. Can be partial or exact match.
//     * <p>
//     * <b>Gherkin Examples:</b>
//     * <ul>
//     * <li>I verify the Name column of the csv contains the text Sam</li>
//     * <li>I verify the Timezone column of the csv has the text EST </li>
//     * <li>I verify the Airport Code column of the CSV does not contain the text RDU</li>
//     * <li>I verify the Airport Code column of the CSV does not have the text RDU</li>
//     * </ul>
//     * @param column String Name of the column to verify
//     * @param assertion String Assertion dictating if we are checking if the column does contain or does not contain
//     * @param textToMatch String the key to retrieve the text to match from the configuration manager
//     */
//    @Then("^I verify the (.*?) column of the (?:csv|CSV)( do(?:es)? not)? (has|have|contains?) the text (.*)$")
//    public static void verifyTextAppearsInColumn(String column, String assertion, String matchType, String textToMatch) {
//        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();
//        boolean negate = !StringUtils.isEmpty(assertion);
//        String errorMessage;
//        boolean partialMatch = matchType.contains(CONTAIN);
//
//        errorMessage = SentinelStringUtils.format("Expected the {} column of the CSV to contain any cells with the text {}", column, textToMatch);
//        String firstColumnCharacter = column.substring(0, 1);
//        if(StringUtils.isNumeric(firstColumnCharacter)){
//            if (negate) {
//                assertFalse(errorMessage, file.verifyAnyColumnCellContains(SentinelStringUtils.parseOrdinal(column), textToMatch, partialMatch));
//            } else {
//                assertTrue(errorMessage, file.verifyAnyColumnCellContains(SentinelStringUtils.parseOrdinal(column), textToMatch, partialMatch));
//            }
//        }
//        else{
//            if (negate) {
//                assertFalse(errorMessage, file.verifyAnyColumnCellContains(column, textToMatch, partialMatch));
//            } else {
//                assertTrue(errorMessage, file.verifyAnyColumnCellContains(column, textToMatch, partialMatch));
//            }
//        }
//    }
//
//    /**
//     * Verifies the CSV has the given number of rows of data, not including the header rows.
//     *  <p>
//     * <b>Gherkin Examples:</b>
//     * <ul>
//     * <li>I verify the csv has 1 data row</li>
//     * <li>I verify the CSV has 22 data rows</li>
//     * </ul>
//     * @param numRows int the number of rows to verify against.
//     */
//    @Then("^I verify the (?:CSV|csv) has (\\d+) data rows?")
//    public static void verifyNumberOfRows(int numRows){
//        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();
//        String errorMessage = SentinelStringUtils.format("Expected the CSV to have {} rows, not including headers.", numRows);
//        assertEquals(errorMessage, numRows, file.getNumberOfDataRows());
//    }

}