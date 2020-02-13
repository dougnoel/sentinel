package com.dougnoel.sentinel.apis;

import java.util.HashMap;
import java.util.Map;

import com.dougnoel.sentinel.exceptions.ConfigurationNotFoundException;
import com.dougnoel.sentinel.exceptions.PageNotFoundException;

public class APIManager {
	private static Map<String, API> apis = new HashMap<String, API>();
	
	private APIManager() {
		// Exists only to defeat instantiation.
	}
	
	/**
	 * Stores an API using the passed API name to instantiate it, and the passed uid as a key.
	 * @param uid String the unique identifier to use when storing the passed API
	 * @param apiName String the name of the sentinel API object to create and store
	 * @return API returns the API object for object chaining
	 * @throws PageNotFoundException if API could not be built
	 * @throws ConfigurationNotFoundException if the value is not found in the configuration file
	 */
	public static API setAPI(String uid, String apiName) throws PageNotFoundException, ConfigurationNotFoundException {
		API api = APIFactory.buildAPI(apiName);
		apis.put(uid, api);
		return api;
	}
	
	public static API getAPI(String uid) {
		return apis.get(uid);
	}
}