package com.dougnoel.sentinel.apis;

import java.util.HashMap;

/**
 * An implementation of the factory design pattern based on the PageFactory.
 *
 * @author dougnoel@gmail.com
 *
 */
public class APIFactory {
	//Track all the APIs we create
	private static HashMap<String, API> apis = new HashMap<>();	
	
	private APIFactory() {
		// Exists only to defeat instantiation
	}
	
	/**
	 * Returns the requested API object by name if we have already created it,
	 * otherwise it creates the API. Note that this creation just instantiates
	 * the object as we late-bind things inside the object.
	 * 
	 * @param apiName String the exact case-sensitive name of the yaml file containing the API information.
	 * @return API the API object that was created or retrieved
	 */
	public static API buildOrRetrieveAPI(String apiName) {
		apiName = apiName.replaceAll("\\s", "");
		API api = apis.get(apiName);
		if (api != null) {
			return api;
		} else {
			api = new API(apiName);
		}
		apis.put(apiName, api);
		return api;
	}
	
}
