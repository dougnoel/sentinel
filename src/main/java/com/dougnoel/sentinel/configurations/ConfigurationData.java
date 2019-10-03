package com.dougnoel.sentinel.configurations;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigurationData extends File {
	/**
	 * Config file for sentinel Configuration Settings. Users can create a sentinel.yml file and we will load
	 * configs from this file instead of setting them manually on the command line each time. A user can override
	 * a specific setting by passing in a command line arg or using System.getProperty() in the setup method in the Test java file.
	 */
	private static final long serialVersionUID = 3930207641065895241L;
	
	@JsonProperty("configurations")
	public Map<String, Map<String, String>> configurations;
	
	@JsonCreator
    public ConfigurationData() throws FileNotFoundException {
        super("conf/sentinel.yml");
    }
	
	/**
	 * Returns the default configuration value for the given key 
	 * 
	 * @param environment String the environment in which sentinel is running (e.g. dev, qa, sit)
	 * @param configurationKey String the name of the setting to retrieve
	 * @return String the configuration value
	 */
	public String getConfigurationValue(String environment, String configurationKey) {
		if(environment == null) {
			environment = "default";
		}
		return configurations.get(environment).get(configurationKey);
	}	
}