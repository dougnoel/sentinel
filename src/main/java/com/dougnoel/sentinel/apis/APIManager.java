package com.dougnoel.sentinel.apis;

import org.openqa.selenium.NotFoundException;

/**
 * Tracks which API is currently being used and requests the APIFactory create it if it does not exist.
 * @author dougnoel@gmail.com
 *
 */
public class APIManager {
	//Only one API should be in use at a time. We are conciously not multi-threading.
	private static API api = null;
	private static Request request = null;
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
			APIManager.api = APIFactory.buildOrRetrieveAPI(apiName);
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

	public static Request getRequest() {
		return request;
	}

	public static void setRequest(Request request) {
		APIManager.request = request;
	}

	public static Response getResponse() {
		return response;
	}

	public static void setResponse(Response response) {
		APIManager.response = response;
	}
}