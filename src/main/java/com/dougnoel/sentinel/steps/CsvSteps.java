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

import java.io.FileNotFoundException;
import java.nio.file.Path;

import static org.junit.Assert.*;


public class CsvSteps {
    private static final Logger log = LogManager.getLogger(CsvSteps.class.getName()); // Create a logger.

    /**
     * Sets the most recently-downloaded file to be the current file-under-test with the given number of header rows.
     * @param numberOfHeaderRows int the number of header rows in the CSV file to test.
     * @throws FileNotFoundException In the case that the file is not found in the location that the DownloadManager specifies.
     */
    @When("^I open a (?:CSV|csv) file with (\\d+) header rows?$")
    public static void openMostRecentlyDownloadedFileAsCsv(int numberOfHeaderRows) throws FileNotFoundException {
        FileManager.setCurrentTestFile(new CsvFile(numberOfHeaderRows));
    }

    /**
     * Sets the given file to be the current file-under-test with the given number of header rows.
     * @param fileLocation String either the location of the file, given by a path, or the name of a testdata object in the current page object.
     * @param numberOfHeaderRows int the number of header rows in the CSV file to test.
     * @throws FileNotFoundException In the case that the file is not found in the location specified.
     */
    @When("^I open (.*) as a (?:CSV|csv) file with (\\d+) header rows?$")
    public static void openSpecificFileAsCsv(String fileLocation, int numberOfHeaderRows) throws FileNotFoundException {
        String filePath;
        try{
            filePath = Configuration.getTestdataValue(fileLocation.trim(), "fileLocation");
        }
        catch(FileException fe){
            filePath = fileLocation;
        }

        FileManager.setCurrentTestFile(new CsvFile(Path.of(filePath), numberOfHeaderRows));
    }

    /**
     * Edits the current CSV file, setting every cell in the given column to the given value.
     * @param column String column the name of the column, or an ordinal (1st, 2nd, 25th, etc.).
     * @param desiredValue String the value to set each cell to.
     */
    @When("^I set all values in the (.*) column to (.*) in the (?:CSV|csv) file$")
    public static void setAllColumnValuesInCsvFile(String column, String desiredValue){
        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();

        String firstColumnCharacter = column.substring(0, 1);
        if(StringUtils.isNumeric(firstColumnCharacter)){
            file.setAllCellsInColumn(SentinelStringUtils.parseLeadingInt(column), desiredValue);
        }
        else{
            file.setAllCellsInColumn(column, desiredValue);
        }
    }

    /**
     * Verifies the current CSV file has or does not have the given text in the given column and given row.
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
        int rowIndex = rowNum.equals("la") ? file.getNumberOfRows() : Integer.parseInt(rowNum);
        boolean partialMatch = matchType.contains("contain");

        var expectedResult = SentinelStringUtils.format(
                "Expected the cell in the {} row and the {} column of the CSV file to {}contain the text {}.",
                SentinelStringUtils.ordinal(rowIndex), column, (negate ? "not " : ""), textToMatch);
        log.trace(expectedResult);

        String firstColumnCharacter = column.substring(0, 1);
        if(StringUtils.isNumeric(firstColumnCharacter)){
            if (negate) {
                assertNotNull(expectedResult, file.verifyCellDataContains(rowIndex, SentinelStringUtils.parseLeadingInt(column), textToMatch, partialMatch));
            } else {
                assertNull(expectedResult, file.verifyCellDataContains(rowIndex, SentinelStringUtils.parseLeadingInt(column), textToMatch, partialMatch));
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
     * Verifies all cells in the given column of the current CSV file have or do not have the given text value.
     * @param column String column the name of the column, or an ordinal (1st, 2nd, 25th, etc.).
     * @param assertion String if null is passed, looks for match(es), if any strong value is passed, looks for the value to not exist.
     * @param matchType String whether we are doing an exact match or a partial match.
     * @param textToMatch String the text to look for in the cell.
     */
    @Then("^I verify all cells in the the (.*) column (?:of|in) the (?:CSV|csv) file( do(?:es)? not)? (has|have|contains?) the value (.*)$")
    public static void verifyCsvAllColumnCellsHaveValue(String column, String assertion, String matchType, String textToMatch){
        CsvFile file = (CsvFile) FileManager.getCurrentTestFile();
        boolean negate = !StringUtils.isEmpty(assertion);
        boolean partialMatch = matchType.contains("contain");

        var expectedResult = SentinelStringUtils.format(
                "Expected all cells in the {} column of the CSV file to {}contain the text {}.",
                            column, (negate ? "not " : ""), textToMatch);
        log.trace(expectedResult);

        String firstColumnCharacter = column.substring(0, 1);
        if(StringUtils.isNumeric(firstColumnCharacter)){
            if (negate) {
                assertFalse(expectedResult, file.verifyAllColumnCellsContain(SentinelStringUtils.parseLeadingInt(column), textToMatch, partialMatch));
            } else {
                assertTrue(expectedResult, file.verifyAllColumnCellsContain(SentinelStringUtils.parseLeadingInt(column), textToMatch, partialMatch));
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

}