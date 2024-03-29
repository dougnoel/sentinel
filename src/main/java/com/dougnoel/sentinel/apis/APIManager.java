package com.dougnoel.sentinel.apis;

import org.openqa.selenium.NotFoundException;

import com.dougnoel.sentinel.enums.RequestType;
import com.dougnoel.sentinel.system.TestManager;

import java.io.InputStream;

/**
 * Tracks which API is currently being used and requests the APIFactory create it if it does not exist.
 * @author dougnoel@gmail.com
 *
 */
public class APIManager {
	//Only one API should be in use at a time. We are consciously not multi-threading.
	private static API api = null;
	private static Response response = null;
	
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
	 * Sets the body to a multipart/form-data body, using the given name, boundary, input stream, and filename.
	 * Also sets a header with the Content-Type set to 'multipart/form-data' and specifies the boundary string passed to this method.
	 * @param nameOfInput String name of the multipart segment. Sometimes applications require this parameter to be a specific value in order to accept file uploads.
	 * @param boundary String the multipart boundary. This method also adds a header specifying this value.
	 * @param inputStream InputStream input stream of the file to upload.
	 * @param filename String name of the file being uploaded.
	 */
	public static void setMultipartFormDataBody(String nameOfInput, String boundary, InputStream inputStream, String filename) {
		getAPI().getRequest().setMultipartFormDataBody(nameOfInput, boundary, inputStream, filename);
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
		getAPI().getRequest().createAndSendRequest(type, endpoint);
	}
	
	/**
	 * Returns the most recent response.
	 * @return Response the response
	 */
	public static Response getResponse() {
		return response;
	}

	/**
	 * Sets the most recent response.
	 * @param response Response the response
	 */
	public static void setResponse(Response response) {
		APIManager.response = response;
	}
}