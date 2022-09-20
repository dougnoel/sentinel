package com.dougnoel.sentinel.apis;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import com.dougnoel.sentinel.exceptions.FileException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * The APIData class is a singleton class that encapsulates page configuration data into a usable  
 * java class. It contains getter methods for api urls, and account data based on the given environment 
 * or account data based on the given environment and the account map within that environment.
 */
public class APIData {
	public Map<String,String> urls;
	public Map<String,Map<String,Map<String,String>>> accounts;
	public Map<String,Map<String,Map<String,String>>> testdata;
	public String include;

	/**
	 * Returns APIData for the given fileName as a string.
	 * 
	 * @see APIData#loadYaml(File)
	 * @param fileName String the name of the page configuration file
	 * @return APIData the configured APIData 
	 * @throws IOException if the configuration file cannot be opened or read
	 */
	public static APIData loadYaml(String fileName) throws IOException{
		return loadYaml(new File(fileName));
	}
	
	/**
	 * Returns the usable APIData object from the given File object.
	 * 
	 * @param fileName File the File object to which the configurations will be mapped.
	 * @return APIData the configured APIData  
	 * @throws IOException if the configuration file cannot be opened or read
	 */
	public static APIData loadYaml(File fileName) throws IOException{
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
    	return null;
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

}
