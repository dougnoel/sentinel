package com.dougnoel.sentinel.apis;

import java.util.HashMap;
import java.util.Map;

public class RequestManager {
	private static Map<String, Request> requests = new HashMap<>();
	
	private RequestManager() {
		// Exists only to defeat instantiation.
	}
	
	/**
	 * Builds and stores a new Request object using the JSON passed to it, 
	 * using the action name as the key.
	 * <p>
	 * NOTE: This will overwrite any existing Request object for the action that already exists.
	 * 
	 * @param uid String the unique identifier to use when storing the Request object created
	 * @param jsonRequest String the full JSON request to be set in the Request object
	 * @return Request the Request object for object chaining
	 */
	public static Request setRequest(String jsonRequest) {
		Request request = new Request(jsonRequest);
		requests.put(uid, request);
		return request;
	}
	
	public static Request getRequest(String uid) {
		return requests.get(uid);
	}
	
	public static void addHeader(String uid, String key, String value) {
		Request request = requests.get(uid);
		if (request == null) {
			request = new Request();
		}
		request.addHeader(key, value);
		requests.put(uid, request);
	}
	
	public static void addRequestParameter(String uid, String key, String value) {
		Request request = requests.get(uid);
		if (request == null) {
			request = new Request();
		}
		request.addRequestParameter(key, value);
		requests.put(uid, request);
	}

}