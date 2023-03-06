package com.dougnoel.sentinel.apis;

import org.openqa.selenium.NotFoundException;

import com.dougnoel.sentinel.enums.RequestType;
import com.dougnoel.sentinel.system.TestManager;

import io.cucumber.java.Scenario;

/**
 * Tracks which API is currently being used and requests the APIFactory create it if it does not exist.
 * @author dougnoel@gmail.com
 *
 */
public class APIManager {
	//Only one API should be in use at a time. We are consciously not multi-threading.
	private static API api = null;
	private static Response response = null;
	private static Scenario scenario = null;
	
	private APIManager() {
		// Exists only to defeat instantiation.
	}
	
	/**
	 * Stores an API using the passed API name to instantiate it.
	 * @param apiName String the name of the sentinel API object to create and store
	 */
	public static void setAPI(String apiName) {
		try {
			api = APIFactory.buildOrRetrieveAPI(apiName);
			TestManager.setActiveTestObject(api);
		} catch (NullPointerException npe) {
			api = null;
		}
	}
	
	/**
	 * Returns the currently active API
	 * @return API Currently selected API by the tester.
	 */
	public static API getAPI() {
		if (api == null)
			throw new NotFoundException("API not set yet. It must be created to before it can be used.");
		return APIManager.api;
	}
	
	/**
	 * Sets the body of a new request for the current API
	 * @param body String the body in the form of json
	 */
	public static void setBody(String body) {
		getAPI().getRequest().setBody(body);
	}

	/**
	 * Set a parameter and its value for a request. They will show up as part of the query string in the API request.
	 * @param parameter String the parameter being passed
	 * @param value String the value to be passed
	 */
	public static void addParameter(String parameter, String value) {
		getAPI().getRequest().addParameter(parameter, value);
	}

	/**
	 * Set a header and its value for a request.
	 * @param name String the name being passed
	 * @param value String the value to be passed
	 */
	public static void addHeader(String name, String value) {
		getAPI().getRequest().addHeader(name, value);
	}

	/**
	 * Send a request of the given type. The response will be stored in a Response object
	 * that the APIManager can retrieve.
	 * @param type com.dougnoel.sentinel.enums.RequestType the type of request to send
	 * @param endpoint the endpoint to send the request
	 */
	public static void sendRequest(RequestType type, String endpoint) {
		response = getAPI().getRequest().createAndSendRequest(type, endpoint);
		getAPI().setResponse(endpoint, response);
	}
	
	/**
	 * Returns the most recent response.
	 * @return com.dougnoel.sentinel.apis.Response the Response object requested
	 */
	public static Response getResponse() {
		return response;
	}
	
	/**
	 * Returns the response stored for the given endpoint under the currently active API.
	 * 
	 * @param endpoint String the name of the endpoint the request was sent to
	 * @return com.dougnoel.sentinel.apis.Response the Response object requested
	 */
	public static Response getResponse(String endpoint) {
		return getAPI().getResponse(endpoint);
	}

	/**
	 * @return Scenario the scenario
	 */
	public static Scenario getScenario() {
		return scenario;
	}

	/**
	 * @param scenario Scenario the scenario to set
	 */
	public static void setScenario(Scenario scenario) {
		APIManager.scenario = scenario;
	}
	
	
	
}