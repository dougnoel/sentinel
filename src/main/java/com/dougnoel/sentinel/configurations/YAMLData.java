package com.dougnoel.sentinel.configurations;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;

import com.dougnoel.sentinel.apis.APIData;
import com.dougnoel.sentinel.exceptions.FileException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * The YAMLData class stores common data structures between page and api object yaml files to reduce code duplication.
 * @author dougnoel
 *
 */
public class YAMLData {
	public Map<String,String> urls;
	public Map<String,Map<String,Map<String,String>>> accounts;
	public String include;
	public Map<String,Map<String,Map<String,String>>> testdata;
	
	/**
	 * Returns YAMLData for the given fileName as a string.
	 * 
	 * @see YAMLData#loadYaml(File)
	 * @param fileName String the name of the page configuration file
	 * @return YAMLData the configured YAMLData 
	 * @throws IOException if the configuration file cannot be opened or read
	 */
	public static YAMLData loadYaml(String fileName) throws IOException{
		return loadYaml(new File(fileName));
	}
	
	/**
	 * Returns the usable YAMLData object from the given File object.
	 * 
	 * @param fileName File the File object to which the configurations will be mapped.
	 * @return YAMLData the configured APIData  
	 * @throws IOException if the configuration file cannot be opened or read
	 */
	public static YAMLData loadYaml(File fileName) throws IOException{
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
				.configure(DeserializationFeature
				.FAIL_ON_UNKNOWN_PROPERTIES, false);
		APIData pageData = null;
		try {
			pageData = mapper.readValue(fileName, APIData.class);
		} catch (Exception e) {
			throw new FileException(e, fileName);
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
    public Map<String,String> getAccount(String env, String account) {
    	if (accounts.containsKey(env)) {
    		return accounts.get(env).get(account);
    	}
    	return Collections.emptyMap();
    }
    
    /**
     * Returns any other places we might be including test data from.
     * 
     * @return String[] list of page parts
     */
    public String[] getPageParts() {
    	if (StringUtils.isNotBlank(include)) {
    		return include.trim().split(",");
    	}
    	return new String[0];
    }
    
    /**
     * Returns whether or not a URL exists for the given environment
     * in the API object.
     * 
     * @param env String the environment to check
     * @return boolean true if found, otherwise false
     */
    public boolean containsUrl(String env) {
    	return urls.containsKey(env);
    }
    
    /**
     * Returns a URL based on the environment passed.
     * 
     * @param env String the environment to check
     * @return String the URL
     */
    public String getUrl(String env) {
    	return urls.get(env);
    }
    
    /**
	 * Returns testdata data for the given environment and the given dataobject (named in the YAML page object).
	 * <p>Example of test data:</p>
	 * <pre>
	 * testdata:
	 *   alpha:
	 *     report:
	 *       id: 123456
	 *       version: 1
	 * </pre>
	 * 
	 * @param env String the desired environment (qa, sit, etc.)
	 * @param dataObject String the requested dataobject
	 * @return Map&lt;String, String&gt; the dataobject data, or null if the requested environment doesn't exist
	 */
    public Map<String,String> getTestdata(String env, String dataObject) {
    	if (testdata != null && testdata.containsKey(env)) {
    		return testdata.get(env).get(dataObject);
    	}
    	return new ConcurrentHashMap<>();
    }
}
