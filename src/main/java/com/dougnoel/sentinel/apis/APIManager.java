package com.dougnoel.sentinel.apis;

import java.util.HashMap;
import java.util.Map;

import com.dougnoel.sentinel.exceptions.ConfigurationMappingException;
import com.dougnoel.sentinel.exceptions.ConfigurationParseException;
import com.dougnoel.sentinel.exceptions.FileNotFoundException;
import com.dougnoel.sentinel.exceptions.IOException;
import com.dougnoel.sentinel.exceptions.MissingConfigurationException;
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
	 * @throws ConfigurationParseException if error thrown while reading configuration file into sentinel
	 * @throws ConfigurationMappingException if error thrown while mapping configuration file to sentinel
	 * @throws MissingConfigurationException if the requested configuration property has not been set
	 * @throws IOException if other error occurs when mapping yml file into sentinel
	 * @throws FileNotFoundException if the sentinel configuration file does not exist
	 * @throws PageNotFoundException if API could not be built
	 */
	public static API setAPI(String uid, String apiName) throws ConfigurationParseException, ConfigurationMappingException, MissingConfigurationException, IOException, FileNotFoundException, PageNotFoundException {
		API api = APIFactory.buildAPI(apiName);
		apis.put(uid, api);
		return api;
	}
	
	public static API getAPI(String uid) {
		return apis.get(uid);
	}
}