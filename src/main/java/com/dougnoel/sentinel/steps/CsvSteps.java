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


    @When("^I open the (?:CSV|csv) file with (\\d+) header rows?$")
    public static void openMostRecentlyDownloadedFileAsCsv(int numberOfHeaderRows) throws FileNotFoundException {
        FileManager.setCurrentTestFile(new CsvFile(numberOfHeaderRows));
    }

    @When("^I open (?:the )?(.*) as a (?:CSV|csv) file with (\\d+) header rows?$")
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
