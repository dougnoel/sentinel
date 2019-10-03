package com.dougnoel.sentinel.pages;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.exceptions.ConfigurationMappingException;
import com.dougnoel.sentinel.exceptions.ConfigurationParseException;
import com.dougnoel.sentinel.strings.StringUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * The PageData class is a singleton class that encapsulates page configuration data into a usable  
 * java class. It contains getter methods for page urls, and account data based on the given environment or account data 
 * based on the given environment and the account map within that environment.
 */
public class PageData {
	private static final Logger log = LogManager.getLogger(PageData.class); // Create a logger.
	// page urls to load in the web driver
	public Map<String,String> urls;
	// user account data 
	public Map<String,Map<String,String>> account;
	// environment specific user account data
	public Map<String,Map<String,Map<String,String>>> accounts;
	// data object
    public Map<String,Map<String,String>> data;
    // test data object
    public Map<String,Map<String,Map<String,String>>> testdata;
	

	/**
	 * Returns PageData for the given fileName as a string.
	 * 
	 * @see PageData#loadYaml(File)
	 * @param fileName String the name of the page configuration file
	 * @return PageData the configured PageData 
	 * @throws ConfigurationParseException if error occurs when parsing page configuration data
	 * @throws ConfigurationMappingException if error occurs when mapping page configuration data
	 * @throws IOException if the configuration file cannot be opened or read
	 */
	public static PageData loadYaml(String fileName) throws ConfigurationParseException, ConfigurationMappingException, IOException{
		return loadYaml(new File(fileName));
	}
	
	/**
	 * Returns the usable PageData object from the given File object.
	 * 
	 * @param fileName File the File object to which the configurations will be mapped.
	 * @return PageData the configured PageData
	 * @throws ConfigurationParseException if error occurs when parsing page configuration data
	 * @throws ConfigurationMappingException if error occurs when mapping page configuration data
	 * @throws IOException if the configuration file cannot be opened or read
	 */
	public static PageData loadYaml(File fileName) throws ConfigurationParseException, ConfigurationMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		PageData pageData = null;
		try {
			pageData = mapper.readValue(fileName, PageData.class);
		} catch (JsonParseException e) {
			String errorMessage = StringUtils.format("Configuration file is not a valid YAML file: {}.", fileName);
			log.error(errorMessage);
			throw new ConfigurationParseException(errorMessage, e);
		} catch (JsonMappingException e) {
			String errorMessage = StringUtils.format("Incorrect formatting in the configuration file: {}.", fileName);
			log.error(errorMessage);
			throw new ConfigurationMappingException(errorMessage, e);
		}
			
		return pageData;
		
	}

	/**
	 * Returns account data for the given environment
	 * 
	 * @param env String the desired environment (qa, sit, etc.)
	 * @return Map&lt;String,String&gt; a map of the user account data.
	 */
	public Map<String,String> getAccount(String env) {
	    return account.get(env);
	}
	
	/**
	 * Returns account data for the given environment and the given account, e.g. If an environment has more than one account,
	 * Sentinel can run tests for any number of those accounts.
	 * 
	 * @param env String the desired environment (qa, sit, etc.)
	 * @param account String the requested account 
	 * @return Map&lt;String, String&gt; the user account data
	 */

    public Map<String,String> getAccount(String env, String account) {
        return accounts.get(env).get(account);
    }
    
    /**
     * Returns test data for the given environment
     * 
     * @param env String the desired environment (Dev, sit, etc.)
     * @return Map&lt;String, String&gt; the data for the given environment
     */
    public Map<String,String> getTestData(String env) {
	    return data.get(env);
	}
	
    /**
     * Returns the value for then given key from the test data object for the given environment
     * @param env String the desired environment (dev, qa, sit, etc.)
     * @param key String the key for the desired value
     * @return Map&lt;String, String&gt; the test data
     */
    public Map<String,String> getTestData(String env, String key) {
        return testdata.get(env).get(key);
    }
}
