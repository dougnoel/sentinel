package com.dougnoel.sentinel.databases;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import com.dougnoel.sentinel.apis.APIManager;
import com.dougnoel.sentinel.configurations.Configuration;
import com.dougnoel.sentinel.configurations.YAMLData;
import com.dougnoel.sentinel.pages.Page;
import com.dougnoel.sentinel.system.FileManager;
import com.dougnoel.sentinel.system.YAMLObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * The DatabaseData class is a singleton class that encapsulates page configuration data into a usable  
 * java class. It contains getter methods for page urls, and account data based on the given environment or account data 
 * based on the given environment and the account map within that environment.
 */
public class DatabaseData extends YAMLData {
	private static final Logger log = LogManager.getLogger(DatabaseData.class); // Create a logger.
	// page urls to load in the web driver TODO: Annotate corretly.
	public static Map<String,String> urls;
	// user account data TODO: Annotate corretly.
	public Map<String,Map<String,Map<String,String>>> accounts;
	public Map<String,Map<String,String>> tables;
	public String include;

	/**
	 *
	 * Returns PageData for the given fileName as a string.
	 * @see DatabaseData#loadYaml(File)
	 * @param fileName String the name of the page configuration file
	 * @return PageData the configured PageData 
	 * @throws IOException if the configuration file cannot be opened or read
	 */
	public static DatabaseData loadYaml(String fileName) throws IOException {
		return loadYaml(FileManager.findFilePath(fileName));
	}
	
	/**
	 * Returns the usable PageData object from the given File object.
	 * 
	 * @param fileName File the File object to which the configurations will be mapped.
	 * @return PageData the configured PageData
	 * @throws IOException if the configuration file cannot be opened or read
	 */
	public static DatabaseData loadYaml(File fileName) throws IOException {
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		DatabaseData pageData = null;
		try {
			pageData = mapper.readValue(fileName, DatabaseData.class);
		} catch (Exception e) {
			//throw new YAMLFileException(e, fileName);
			throw e;
		}

		return pageData;
	}
	
	/**
	 * Returns account data for the given environment and the given account, e.g. If an environment has more than one account,
	 * Sentinel can run tests for any number of those accounts.
	 * 
	 * @param env String the desired environment (qa, sit, etc.)
	 * @param account String the requested account 
	 * @return Map&lt;String, String&gt; the user account data, or null if the requested environment doesn't exist
	 */
	@Override
    public Map<String,String> getAccount(String env, String account) {
    	if (accounts.containsKey(env)) {
    		return accounts.get(env).get(account);
    	}
    	return null;
    }
    
    /**
     * Returns an element if it exists in a page object.
     * @param elementName the name of the element in the page object under the 'elements' section
     * @return Map&lt;String, String&gt; the locators for an element
     */
    public Map<String,String> getElement(String elementName) {
    	if (tables.containsKey(elementName)) {
    		return tables.get(elementName);
    	}
    	return null;
    }
    
    /**
     * Returns any page parts to search for elements
     * @return String[] list of page parts
     */
    public String[] getPageParts() {
    	if (StringUtils.isNotBlank(include)) {
    		return include.toString().trim().split(",");
    	}
    	return new String[0];
    }
    
   /* public static boolean containsUrl(String env) {
    	return urls.containsKey(env);
    }*/
    
    public static String getDatabase(String env) {
    	return urls.get(env);
		//Configuration.getURL();

    }

}
