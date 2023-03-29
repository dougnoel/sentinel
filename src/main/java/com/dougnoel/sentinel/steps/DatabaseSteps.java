package com.dougnoel.sentinel.steps;

import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.databases.DatabaseData;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.system.DownloadManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.databases.Database;
import com.dougnoel.sentinel.databases.DatabaseFactory;
import com.dougnoel.sentinel.databases.DatabaseManager;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;

import static com.dougnoel.sentinel.elements.ElementFunctions.getElement;
import static org.junit.Assert.*;

/**
 * Methods used to define basic operations. Other step files can extend or
 * include this one to leverage these actions.
 * 
 * Functionality in this class includes clicking given elements, entering text, selecting items, verifying elements exist, 
 * contains given text, or are active, enabled, or hidden, verifying table columns or table rows have given text, 
 * unique text, text for stored values, navigating results, 
 * and basic browser interaction.
 */
public class DatabaseSteps {
    private static final Logger log = LogManager.getLogger(DatabaseSteps.class.getName()); // Create a logger.
    
    @Given("^I connect to the (.*?)$")
    public static void setDatabase(String databaseName) throws IOException {
    	DatabaseManager.setDatabaseConnection(databaseName);
        Configuration.getURL(DatabaseManager.getCurrentDatabaseConnection());
        log.trace("Current Database Connection: {}", databaseName);
    }

    @Given("^I use the (.*?) database as (.*)$")
    public static void useDatabase(String databaseName, String userName) {
    	DatabaseManager.useDatabase(databaseName);
        DatabaseManager.userName = userName;
    }
    
    @When("I submit the query")
    public static void submitQuery(String queryString) {
    	DatabaseManager.useDatabase(DatabaseManager.getDatabaseInUse());
        Database db = DatabaseManager.getCurrentDatabaseConnection();
        db.loadDriver();
        db.getConnection();
        db.query(queryString);
    	//Close the connection - TRIGGER THIS IN AN AFTER STEP
    }

    @Then("^I verify the query result( does not)? (has|have|contains?) the text (.*)$")
    public static void verifyQueryResult(String assertion, String matchType, String text) throws IOException {
        boolean negate = !StringUtils.isEmpty(assertion);
        String negateText = negate ? "not " : "";
        boolean partialMatch = matchType.contains("contain");
        String partialMatchText = partialMatch ? "contain" : "exactly match";

        Database db = DatabaseManager.getCurrentDatabaseConnection();
        String actualResponse = db.getLastResult();

        String expectedResult;
        expectedResult = SentinelStringUtils.format(
                "Expected the last query response to {}{} the text {}. The response contained the text: {}",
                negateText, partialMatchText, text, actualResponse
                        .replace("\n", " "));
        log.trace(expectedResult);

        if (partialMatch) {
            if (negate) {
                assertFalse(expectedResult, actualResponse.contains(text));
            } else {
                assertTrue(expectedResult, actualResponse.contains(text));
            }
        } else {
            if (negate) {
                assertFalse(expectedResult, StringUtils.equals(actualResponse, text));
            } else {
                assertTrue(expectedResult, StringUtils.equals(actualResponse, text));
            }
        }
    }
}