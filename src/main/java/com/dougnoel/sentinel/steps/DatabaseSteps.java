package com.dougnoel.sentinel.steps;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.databases.DatabaseManager;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

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
    
    @Given("I connect to the {string}")
    public void setDatabase(String databaseName) {
    	String normalizedDatabaseName = databaseName.replaceAll("\\s", "") + "Page";
    	DatabaseManager.setDatabase(normalizedDatabaseName);
        log.debug("Current Connection {}", normalizedDatabaseName);
    }

    @Given("I use the {string} database")
    public void useDatabase(String databaseName) {
    	DatabaseManager.useDatabase(databaseName);
    	log.debug("Current Database {}", databaseName);
    }
    
    @When("I submit the query")
    public void i_submit_the_query(String queryString) {
        //Connect to DB
    	//Select the database
    	//Run the Query
    	//Store the return result in last_result
    	//Close the connection - TRIGGER THIS IN AN AFTER STEP
    }

    @Then("I should get the result {string}")
    public void i_should_get_the_result(String string) {
        //rEtrieve the results and compare them
    }

    @When("I run the update")
    public void i_run_the_update(String updateString) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("I should get a success message")
    public void i_should_get_a_success_message() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
    
}