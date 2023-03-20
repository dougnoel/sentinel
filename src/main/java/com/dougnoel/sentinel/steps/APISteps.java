package com.dougnoel.sentinel.steps;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.Duration;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.apis.APIManager;
import com.dougnoel.sentinel.apis.Response;
import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.enums.RequestType;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class APISteps {
	private static final Logger log = LogManager.getLogger(APISteps.class.getName()); // Create a logger.
	
	/**
	 * Loads an API based on the environment you are currently testing.
     * Refer to the documentation in the sentinel.example project for more information. 
     * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I use the API named Agify API</li>
     * <li>I use the API named My API</li>
     * </ul>
     * <p>
     * 
	 * @param apiName name of the API object we want to use
	 */
	@Given("^I use the API named (.*?)$")
	public static void setAPI(String apiName) {
        APIManager.setAPI(apiName);
	}

	/**
	 * Sets the body of the active API call to the string passed.
	 * 
	 * @param body String the json to be passed as the body of the request.
	 */
	@Given("I set the request body to")
	public static void setRequestBody(String body) {
		APIManager.setBody(SentinelStringUtils.replaceStoredVariables(body));
        log.trace("Body passed: {}", body);
	}

	/**
	 * Loads the indicated testdata located in the API object yaml file to use
	 * as the json for the body of the request.
	 * 
	 * @param testdataName String the name of the testdata entry to use
	 */
	@Given("^I load (.*?) to use as the request body$")
	public static void loadRequestBody(String testdataName) {
		String body = Configuration.getTestData(testdataName, "json");
        APIManager.setBody(body);
        log.trace("Body passed: {}", body);
	}
	
	/**
	 * Sets a query string parameter.
	 * 
	 * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I add a status parameter with the value available</li>
     * <li>I add a name parameter with the value Bob</li>
     * <li>I add an address parameter with the value 143 Down Street</li>
     * </ul>
     * <p>
     * 
	 * @param parameter String the parameter to set
	 * @param value String the value to set it
	 */
	@When("^I add an? (.*?) parameter with the value (.*?)$")
	public static void addParameter(String parameter, String value) {
		APIManager.addParameter(parameter, SentinelStringUtils.replaceStoredVariables(value));

	}
	
	/**
	 * Sends a DELETE, GET, POST or PUT request to the specified endpoint.
	 * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I send a GET request to the pet/findByStatus endpoint</li>
     * <li>I send a POST request to the users endpoint</li>
     * <li>I send a PUT request to the amdins endpoint</li>
     * </ul>
     * <p>
     *  
	 * @param apiCallType
	 * @param endpoint
	 */
	@When("^I send a (DELETE|GET|POST|PUT) request to the (.*?) endpoint$")
	public static void sendRequest(String apiCallType, String endpoint) {
		APIManager.sendRequest(RequestType.valueOf(apiCallType), SentinelStringUtils.replaceStoredVariables(endpoint));
	}
	
	/**
	 * Sends a GET or DELETE to the specified endpoint for the indicated record.
	 * 
	 * <p>
     * <b>Gherkin Examples:</b>
     * <ul>
     * <li>I GET record 10 from the pet endpoint</li>
     * <li>I DELETE record bob from the user endpoint</li>
     * </ul>
     * <p>
     *  
	 * @param apiCallType String the type of call to make
	 * @param parameter String the value to send to the endpoint
	 * @param endpoint String the endpoint name as referenced in the swagger file
	 */
	@When("^I (DELETE|GET) record (.*) from the (.*?) endpoint$")
	public static void sendRequest(String apiCallType, String parameter, String endpoint) {
		sendRequest(apiCallType, endpoint + "/" + SentinelStringUtils.replaceVariable(parameter));
	}
	
	/**
	 * Verify that the response code returned from the last API call is what we expect.
	 * https://developer.mozilla.org/en-US/docs/Web/HTTP/Status
	 * 
	 * @param statusCode int the status code expected
	 */
	@When("^I verify the response code equals (\\d{3})$")
	public static void verifyResponseCodeEquals(int statusCode) {
		Response response = APIManager.getResponse();
		int responseCode = response.getResponseCode();
		var expectedResult = SentinelStringUtils.format("Expected the response code to be {}, and it was {}.\nFull response:\n{}",
				statusCode, responseCode, response.getResponse());
		assertTrue(expectedResult, statusCode == responseCode);
	}
	
	@When("^I verify the response was received in less than (\\d{1,2}(?:[.,]\\d{1,4})?) seconds?$")
	public static void verifyResponseTime(double time) {
		Duration timeLimit = Duration.ofMillis((long) (time * 1000));
		Duration responseTime = APIManager.getResponse().getReponseTime();
		
		String expectedResult = SentinelStringUtils.format("Expected the response to take less than {} seconds, but the response took {} ",
				time, responseTime);
		assertTrue(expectedResult, responseTime.compareTo(timeLimit) < 0);
	}

	/**
	 * Validates text in an API response.
	 * 
	 * @param assertion String null to see if the text exists, "does not" to see if it is absent
	 * @param matchType String use "contains" for a partial match otherwise it will be an exact match
	 * @param text String the text to match
	 */
	@Then("^I validate the response( does not)? (has|have|contains?) the text \"([^\"]*)\"$")
    public static void verifyResponseContains(String assertion, String matchType, String text) {
        boolean negate = !StringUtils.isEmpty(assertion);
        boolean partialMatch = matchType.contains("contain");

        int responseCode = APIManager.getResponse().getResponseCode();
        String responseText = APIManager.getResponse().getResponse();
        String expectedResult = SentinelStringUtils.format(
                "Expected the response to {}{} the text {}. The response had a response code of {} and contained the text: {}",
                (negate ? "not " : ""), (partialMatch ? "contain" : "exactly match"), text, responseCode, responseText
                        .replace("\n", " "));
        log.trace(expectedResult);
        if (partialMatch) {
            if (negate) {
                assertFalse(expectedResult, responseText.contains(text));
            } else {
                assertTrue(expectedResult, responseText.contains(text));
            }
        } else {
            if (negate) {
                assertFalse(expectedResult, StringUtils.equals(responseText, text));
            } else {
                assertTrue(expectedResult, StringUtils.equals(responseText, text));
            }
        }
    }

	/**
	 * Adds header into API request
	 *
	 * @param name String name of a header
	 * @param value String value of the header
	 */
	@When("^I add an? (.*?) header with the value (.*?)$")
	public static void addHeader(String name, String value) {
		APIManager.addHeader(name, SentinelStringUtils.replaceStoredVariables(value));
	}

	/**
	 * Adds the parsed string keys and values in Configuration for later use
	 *
	 * @param values String the string with keys and values
	 * Example:
	 *  When I initialize the configuration values as follows
	 *     """
	 *     id: 10
	 *     category_name: puppies
	 *     """
	 *
	 */
	@When("I initialize the configuration values as follows")
	public void iInitializeTheData(String values) {
		var items = values.replace(" ","").split("\n");
		for (var item:items
		) {
			Configuration.update(item.split(":")[0],  item.split(":")[1]);
		}
	}
}
