package com.dougnoel.sentinel.apis;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

import com.dougnoel.sentinel.configurations.YAMLData;
import com.dougnoel.sentinel.exceptions.FileException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * The APIData class is a singleton class that encapsulates page configuration data into a usable  
 * java class. It contains getter methods for api urls, and account data based on the given environment 
 * or account data based on the given environment and the account map within that environment.
 */
public class APIData extends YAMLData {
	public Map<String,String> urls;
	public Map<String,Map<String,Map<String,String>>> accounts;
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

}
