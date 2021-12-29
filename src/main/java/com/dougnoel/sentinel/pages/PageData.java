package com.dougnoel.sentinel.pages;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.NoSuchElementException;

import com.dougnoel.sentinel.exceptions.YAMLFileException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * The PageData class is a singleton class that encapsulates page configuration data into a usable  
 * java class. It contains getter methods for page urls, and account data based on the given environment or account data 
 * based on the given environment and the account map within that environment.
 */
public class PageData {
	// page urls to load in the web driver TODO: Annotate corretly.
	public Map<String,String> urls;
	// user account data TODO: Annotate corretly.
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
				.configure(DeserializationFeature
				.FAIL_ON_UNKNOWN_PROPERTIES, false);
		PageData pageData = null;
		try {
			pageData = mapper.readValue(fileName, PageData.class);
		} catch (Exception e) {
			throw new YAMLFileException(e, fileName);
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
			throw new NoSuchElementException(errorMessage);
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

}
