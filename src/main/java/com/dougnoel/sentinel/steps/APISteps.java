package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.apis.actions.ActionFunctions.getAction;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.apis.API;
import com.dougnoel.sentinel.apis.APIManager;
import com.dougnoel.sentinel.apis.Request;
import com.dougnoel.sentinel.apis.RequestManager;
import com.dougnoel.sentinel.apis.Response;
import com.dougnoel.sentinel.apis.ResponseManager;
import com.dougnoel.sentinel.enums.AuthenticationType;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class APISteps {
	private static final Logger log = LogManager.getLogger(APISteps.class.getName()); // Create a logger.
	private static final String RESPONSE_DEBUG = "Response: {}";
    
	private static String cleanAPIName(String apiName) {
		return apiName.replaceAll("\\s", "") + "API";
	}
	
	/**
	 * Gets a JWT Token and sets it for the curently active API
	 */
	@When("^I grab the JWT$")
	public void i_grab_the_jwt() {
		AuthenticationType authType = AuthenticationType.JWT;
		APIManager.getAPI().setAuthType(authType);
		APIManager.getAPI().setAuthToken();
	}
	
    /**
     * Creates or retrieves an API with the given name.
     * 
     * @param apiName name of the API object to create
     * @throws SentinelException if the API cannot be set
     */
	
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
	public void setAPI(String apiName) {
		apiName = cleanAPIName(apiName);
        APIManager.setAPI(apiName);
	}
	
	@When("^I send a (.*) request$")
	public void sendRequest(String actionName) throws IOException, URISyntaxException {
		// Get an API from the API Manager
		API api = APIManager.getAPI(); //3
		// Get an Request from the Request Manager
		Request request = RequestManager.setRequest(actionName); //3
		// Get the action, send the request and set it in the response manager
		ResponseManager.setResponse(uid, getAction(actionName).sendRequest(request, api)); //3
	}
	
	@Then("^I verify a (success) response code was received$")
	public void verifyResponseCode(String expectedResponse) {
		//Get the response from the response manager
		Response response = ResponseManager.getResponse(uid);
		// Assert the response is not null
		assertTrue(response != null);
		log.trace(RESPONSE_DEBUG, response.getResponse());
	}
	
	@Then("^I verify a response was received$")
	public void validateResponse() {
		//Get the response from the response manager
		Response response = ResponseManager.getResponse(uid);
		// Assert the response is not null
		assertTrue(response != null);
		log.info(RESPONSE_DEBUG, response.getResponse());
	}
	
	@Given("^I make a GET request to the (.*?) API using the (.*?) endpoint$")
	public void getRequest(String apiName, String endpoint) throws IOException, URISyntaxException {
		// Get the API name
		apiName = cleanAPIName(apiName); //1
		// Set the API
		APIManager.setAPI(uid, apiName); //1
		// Set the Request object
		RequestManager.setRequest(uid, "Blah"); //2
		// Get an API from the API Manager
		API api = APIManager.getAPI(uid); //3
		// Get an Request from the Request Manager
		Request request = RequestManager.getRequest(uid); //3
		// Get the action, send the request and set it in the response manager
		ResponseManager.setResponse(uid, getAction(endpoint, uid).sendRequest(request, api)); //3
		//Get the response from the response manager
		Response response = ResponseManager.getResponse(uid); //4
		// Output the response
		log.info(RESPONSE_DEBUG, response.getResponse()); //4
	}
	
	@Given("^I add a header key called (.*) with value (.*) to the request")
	public void addHeader(String headerKey, String headerValue) {
		RequestManager.addHeader(uid, headerKey, headerValue);
	}
	
	@When("^I verify the response code equals (\\d{3})$")
	public void verifyResponseCodeEquals(int statusCode) {
		Integer responseCode = ResponseManager.getResponse(uid).getResponseCode();
		log.trace("I verify response code value: {}", responseCode);
		assertTrue(statusCode == responseCode);
	}
	
	@Then("^I validate the response( does not)? (has|have|contains?) the text \"([^\"]*)\"$")
    public void verifyResponseContains(String assertion, String matchType, String text) {
        boolean negate = !StringUtils.isEmpty(assertion);
        boolean partialMatch = matchType.contains("contain");

        Integer responseCode = ResponseManager.getResponse(uid).getResponseCode();
        String responseText = ResponseManager.getResponse(uid).getResponse();
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
	
}
