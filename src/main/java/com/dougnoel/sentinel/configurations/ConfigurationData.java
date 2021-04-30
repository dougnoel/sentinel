package com.dougnoel.sentinel.configurations;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigurationData extends File {
	/**
	 * Config file for sentinel Configuration Settings. Users can create a sentinel.yml file and we will load
	 * configs from this file instead of setting them manually on the command line each time. A user can override
	 * a specific setting by passing in a command line arg or using System.getProperty() in the setup method in the Test java file.
	 */
	private static final long serialVersionUID = 3930207641065895241L;
	
	@JsonProperty("configurations")
	private Map<String, Map<String, String>> configurations;
	
	@JsonCreator
    public ConfigurationData() throws FileNotFoundException {
        super("conf/sentinel.yml");
    }
	
	/**
	 * Returns the configuration value for the given environment and key .
	 * 
	 * @param environment String the environment in which the test is running (e.g. dev, qa, sit)
	 * @param configurationKey String the name of the setting to retrieve
	 * @return String the configuration value
	 */
	protected String getConfigurationValue(String environment, String configurationKey) {
		String configurationValue = null;

			if (configurations.containsKey(environment)) {
				configurationValue = configurations.get(environment).get(configurationKey);
			}
			// If we did not find a value for the set environment, then we check the default settings
			if (configurationValue == null) {
				configurationValue = configurations.get("default").get(configurationKey);
			}

		return configurationValue;
	}
}