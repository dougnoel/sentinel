package com.dougnoel.sentinel.pages;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.exceptions.ConfigurationMappingException;
import com.dougnoel.sentinel.exceptions.ConfigurationParseException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
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
	// page urls to load in the web driver TODO: Annotate correctly.
	public Map<String,String> urls;
	public Map<String,String> executables;
	// user account data TODO: Annotate correctly.
	public Map<String,Map<String,Map<String,String>>> accounts;
	public Map<String,Map<String,String>> elements;
	public String include;

	/**
	 * Returns PageData for the given fileName as a string.
	 * 
	 * @see PageData#loadYaml(File)
	 * @param fileName String the name of the page configuration file
	 * @return PageData the configured PageData 
	 * @throws IOException if the configuration file cannot be opened or read
	 */
	public static PageData loadYaml(String fileName) throws IOException{
		return loadYaml(new File(fileName));
	}
	
	/**
	 * Returns the usable PageData object from the given File object.
	 * 
	 * @param fileName File the File object to which the configurations will be mapped.
	 * @return PageData the configured PageData
	 * @throws IOException if the configuration file cannot be opened or read
	 */
	public static PageData loadYaml(File fileName) throws IOException{
		ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
				.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		PageData pageData = null;
		try {
			pageData = mapper.readValue(fileName, PageData.class);
		} catch (JsonParseException e) {
			String errorMessage = SentinelStringUtils.format("Page object file is not a valid YAML file: {}.", fileName);
			log.error(errorMessage);
			throw new ConfigurationParseException(errorMessage, e);
		} catch (JsonMappingException e) {
			String errorMessage = SentinelStringUtils.format("Incorrect formatting in the page object file: {}.", fileName);
			log.error(errorMessage);
			throw new ConfigurationMappingException(errorMessage, e);
		}
		if (pageData.urls != null && pageData.executables != null) {
			String errorMessage = SentinelStringUtils.format("A page object cannot contain both urls and executables: {}.", fileName);
			log.error(errorMessage);
			throw new ConfigurationParseException(errorMessage);
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
     * Returns an element if it exists in a page object.
     * @param elementName the name of the element in the page object under the 'elements' section
     * @return Map&lt;String, String&gt; the locators for an element
     */
    public Map<String,String> getElement(String elementName) {
    	if(elements!=null) {
    		if (elements.containsKey(elementName)) {
        		return elements.get(elementName);
        	}
    	} else {
    		var errorMessage = SentinelStringUtils.format("There is no elements section defined in the page object {}. Please make sure that elements defined are under an \"elements:\" section. Refer to the Readme for more information.", PageManager.getPage().getName());
			throw new ConfigurationParseException(errorMessage);
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
    
    public boolean containsUrl(String env) {
    	return urls.containsKey(env);
    }
    
    public String getUrl(String env) {
    	return urls.get(env);
    }
    
    public boolean hasUrls() {
    	return urls != null;
    }
    
    public boolean containsExecutable(String env) {
    	return executables.containsKey(env);
    }
    
    public String getExecutable(String env) {
    	return executables.get(env);
    }

    public boolean hasExecutables() {
    	return executables != null;
    }
    
}
