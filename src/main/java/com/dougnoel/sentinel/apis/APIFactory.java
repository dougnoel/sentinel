package com.dougnoel.sentinel.apis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.configurations.ConfigurationManager;
import com.dougnoel.sentinel.exceptions.ConfigurationNotFoundException;

/**
 * An implementation of the factory design pattern based on the PageFactory. This factory object 
 * is different in that it does not track the APIs created -instead delegating that work to the
 * APIManager. This should make parallelization easier to implement later on.
 * 
 * TODO: Create a configuration option for searching for APIs or get rid of this and search the whole
 * 		 source tree. Need to do a speed comparison.
 * TODO: Create a parent Factory method to handle the searching and instantiation.
 *
 * @author Doug Noël
 *
 */
public class APIFactory {

	private static final Logger log = LogManager.getLogger(APIFactory.class); // Create a logger.
	
	private APIFactory() {
		// Exists only to defeat instantiation
	}
	
	/**
	 * Returns an API object if it exists in the package searched.
	 * @param pageName String the name of the API object class to instantiate
	 * @param packageName String the name of the package to search
	 * @return API the API object if it exists, otherwise null
	 */
	@SuppressWarnings("deprecation")
	private static API findAPIInPackage(String pageName, String packageName) {
		API api = null;
		try {
			api = (API) Class.forName(packageName + "." + pageName).newInstance();
		} catch (InstantiationException e) {
			log.trace("{}.{} API Object creation failed.", packageName, pageName);
			log.trace("java.lang.InstantiationException: {}", e.getMessage());
		} catch (IllegalAccessException e) {
			log.trace("{}.{} API Object creation failed.", packageName, pageName);
			log.trace("java.lang.IllegalAccessException: {}", e.getMessage());
		} catch (ClassNotFoundException e) {
			log.trace("{}.{} API Object creation failed.", packageName, pageName);
			log.trace("java.lang.ClassNotFoundException: {}", e.getMessage());
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
	 * @throws ConfigurationNotFoundException if the value is not found in the configuration file
	 */
	public static API buildAPI(String apiName) throws ConfigurationNotFoundException {
		API api = null;
		String[] pageObjectPackagesList = ConfigurationManager.getPageObjectPackageList();

		for (String apiObjectPackage : pageObjectPackagesList) {
			log.trace("apiObjectPackage: " + apiObjectPackage);
			api = findAPIInPackage(apiName, apiObjectPackage);
			if (api != null) {
				break; // If we have a page object, stop searching.
			}
		}
		if(api == null) {
			throw new ConfigurationNotFoundException("The API you want to test could not be built. At least one Page object package is required to run a test. Please add a pageObjectPackages property to your conf/sentinel.yml configuration file and try again.");
		}
		return api;
	}
	
}
