package com.dougnoel.sentinel.apis;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.exceptions.IOException;
import com.dougnoel.sentinel.pages.Page;

/**
 * An implementation of the factory design pattern based on the PageFactory. This factory object 
 * is different in that it does not track the APIs created -instead delegating that work to the
 * APIManager. This should make parallelization easier to implement later on.
 *
 * @author Doug Noël
 *
 */
public class APIFactory {
	private static HashMap<String, API> apis = new HashMap<>();	
	
	private APIFactory() {
		// Exists only to defeat instantiation
	}
	
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
	
	/**
	 * Returns an API object if it exists in the package searched.
	 * @param pageName String the name of the API object class to instantiate
	 * @param packageName String the name of the package to search
	 * @return API the API object if it exists, otherwise null
	 */
	private static API findAPIInPackage(String pageName, String packageName) {
		API api = null;
		try {
			api = (API) Class.forName(packageName + "." + pageName).getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			log.trace("{}.{} API Object creation failed.\n{}", packageName, pageName, e.getMessage());
		}
		
		return api;
	}
	
	/**
	 * Returns the API Object for the API name. This allows us to operate on APIs without knowing 
	 * they exist when we write step definitions.
	 * <p>
	 * Searches any optional API object packages set with the pageObjectPackages system property, and 
	 * then searches the defaultPackageName value.
	 * <p>
	 * <b>Example:</b>
	 * <p>
	 * <code>System.setProperty("pageObjectPackages", "pages.SharedComponent,pages.UserAdminTool");</code>
	 * @param apiName String the name of the page in <a href="https://en.wikipedia.org/wiki/Camel_case">Pascal case</a>
	 * @return API the specific page object cast as a generic page object
	 */
	public static API buildAPI(String apiName) {
		API api = null;

		api = findAPIInPackage(apiName, "apis");
		if(api == null) {
			throw new IOException("The API you want to test could not be built. At least one Page object package is required to run a test. Please add a pageObjectPackages property to your conf/sentinel.yml configuration file and try again.");
		}
		return api;
	}
	
}
