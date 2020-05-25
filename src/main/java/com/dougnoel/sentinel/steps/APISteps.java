package com.dougnoel.sentinel.steps;

import static com.dougnoel.sentinel.apis.ActionFunctions.getAction;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import com.dougnoel.sentinel.exceptions.SentinelException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;



public class APISteps {
	protected Scenario scenario = null;
	protected String uid = null;
	private static final Logger log = LogManager.getLogger(APISteps.class.getName()); // Create a logger.

    @Before
    public void before(Scenario scenario) {
        this.scenario = scenario;
        uid = scenario.getId();
    }
    
	private static String cleanAPIName(String apiName) {
		return apiName.replaceAll("\\s", "") + "API";
	}
	
	@When("^I grab the JWT$")
	public void i_grab_the_jwt() {
		AuthenticationType authType = AuthenticationType.JWT;
		APIManager.getAPI(uid).setAuthType(authType);
		APIManager.getAPI(uid).setAuthToken();
	}
	
    /**
     * Creates or retrieves an API with the given name for a determined UID.
     * 
     * By using UIDs, we can ensure that parallel tests use the correct API object.
     * 
     * TODO: Think about how to cleanup all these APIs
     * @param apiName name of the API object to create
     * @throws SentinelException if the API cannot be set
     */
	@Given("^I use the (.*?) API$")
	public void i_use_an_API(String apiName) throws SentinelException  {
		apiName = cleanAPIName(apiName);
        log.debug("API Name: {} UID: {}", apiName, uid);
        APIManager.setAPI(uid, apiName);
	}
	
	@When("^I send a request to the (.*?) endpoint$")
	public void i_send_a_request_to_the_endpoint(String endpoint) throws Throwable {
		// Get an API from the API Manager
		API api = APIManager.getAPI(uid); //3
		// Get an Request from the Request Manager
		Request request = RequestManager.getRequest(uid); //3
		// Get the action, send the request and set it in the response manager
		ResponseManager.setResponse(uid, getAction(endpoint, uid).sendRequest(request, api)); //3
	}
	
	@Then("^I verify a (success) response code was received$")
	public void i_verify_the_response_code(String expectedResponse) throws Throwable {
		//Get the response from the response manager
		Response response = ResponseManager.getResponse(uid);
		// Assert the response is not null
		assertTrue(response != null);
		log.trace("Response: {}", response.getResponse());
	}
	
	@Then("^I verify a response was received$")
	public void i_validate_the_response() throws Throwable {
		//Get the response from the response manager
		Response response = ResponseManager.getResponse(uid);
		// Assert the response is not null
		assertTrue(response != null);
		log.info("Response: {}", response.getResponse());
	}
	
	@Given("^I make a GET request to the (.*?) API using the (.*?) endpoint$")
	public void i_make_a_GET_request_to_an_API(String apiName, String endpoint) throws Throwable {
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
		log.info("Response: {}", response.getResponse()); //4
	}
	
//	
//	@Given("^I make a POST request to the (.*?) with the data (.*?)$")
//	public void i_make_a_POST_request_to_an_API_with_data(String APIName, String data) throws Throwable {
//		APIManager.doPostAndPrintResultToConsole(APIName, data);
//	}
	
	@Given("^I add a header key called (.*) with value (.*) to the request")
	public void i_add_header_pairs_to_request(String headerKey, String headerValue) {
		RequestManager.addHeader(uid, headerKey, headerValue);
	}

	@Given("^My request it (not) authenticated ?:with (a jwt|an auth_token)")
	public void i_add_authorization(String noAuth, String token) {
//		if(noAuth == null) {
//			API.authenticationType = token;
//		} else {
//			API.authenticantionType = "NONE";
//		}
	}

	@Given("^I have a (GET|POST|PUT) HTTP Request")
	public void i_make_a_request(String RequestType) {
//		switch(RequestType) {
//			case "GET":
//				return new GET();
//			case "POST":
//				return new POST();
//			case "PUT":
//				return new PUT();
//			default:
//				throw new RuntimeException("Invalid request type passed to function. Please choose GET, POST, or PUT as your request type");
//		}
//		
	}
	
	@When("^I send the request$") 
	public void i_send_the_request(){
//		request.sendRequest();
	}
	
	@When("^I (check|verify|expect) response from GET List$")
	public void i_verify_response_from_GET_list() {
		//assertTrue(response, expectedResponse)
	}
	
	@When("^I verify the response code equals (\\d{3})$")
	public void i_verify_the_response_code_equals(int statusCode) {
		Integer responseCode = ResponseManager.getResponse(uid).getResponseCode();
		log.trace("I verify response code value: {}", responseCode);
		assertTrue(statusCode == responseCode);
	}
	
	@Then("^I validate the response( does not)? (has|have|contains?) the text \"([^\"]*)\"$")
    public void i_verify_the_response_contains_the_text(String assertion, String matchType,
            String text)
            throws Throwable {
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
