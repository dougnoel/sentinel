package com.dougnoel.sentinel.apis;

import java.util.HashMap;
import java.util.Map;

public class APIManager {
	private static Map<String, API> apis = new HashMap<>();
	
	private APIManager() {
		// Exists only to defeat instantiation.
	}
	
	/**
	 * Stores an API using the passed API name to instantiate it, and the passed uid as a key.
	 * @param uid String the unique identifier to use when storing the passed API
	 * @param apiName String the name of the sentinel API object to create and store
	 * @return API returns the API object for object chaining
	 */
	public static API setAPI(String uid, String apiName) {
		API api = APIFactory.buildAPI(apiName);
		apis.put(uid, api);
		return api;
	}
	
	public static API getAPI(String uid) {
		return apis.get(uid);
	}
}