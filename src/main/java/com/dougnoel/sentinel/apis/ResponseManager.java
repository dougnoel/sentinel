package com.dougnoel.sentinel.apis;

import java.util.HashMap;
import java.util.Map;

public class ResponseManager {
	private static Map<String, Response> responses = new HashMap<>();
	
	private ResponseManager() {
		// Exists only to defeat instantiation.
	}
	
	/**
	 * Stores a new Response object using the uid passed as the key.
	 * @param uid String the unique identifier to use when storing the Response object 
	 * @param response Response the Sentinel Response object to be stored
	 * @return Response the original Response object for object chaining
	 */
	
	public static Response setResponse(String uid, Response response) {
		responses.put(uid, response);
		return response;
	}
	
	public static Response getResponse(String uid) {
		return responses.get(uid);
	}

}