package com.dougnoel.sentinel.configurations;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

import com.dougnoel.sentinel.exceptions.FileException;
import com.dougnoel.sentinel.exceptions.IOException;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.system.FileManager;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ConfigurationData {
	/**
	 * Config file for sentinel Configuration Settings. Users can create a sentinel.yml file and we will load
	 * configs from this file instead of setting them manually on the command line each time. A user can override
	 * a specific setting by passing in a command line arg or using System.getProperty() in the setup method in the Test java file.
	 */
	private static final long serialVersionUID = 3930207641065895241L;
	private static final String CONFIG_FILE_NAME = "sentinel.yml";
	private File configFile;
	private String configFilePath;

	@JsonProperty("configurations")
	private Map<String, Map<String, String>> configurations;

	@JsonCreator
	public ConfigurationData() throws FileNotFoundException {
		try {
			setConfigFile(FileManager.findFilePath(CONFIG_FILE_NAME));
			setConfigFilePath(getConfigFile());
			configFilePath = configFile.getAbsolutePath();
		} catch (FileException e) {
			String warningMessage = SentinelStringUtils.format("Could not locate the configuration file {} in the project. Settings present in the file will not be honored.", CONFIG_FILE_NAME);
			throw new IOException(warningMessage , e);
		}
	}

	private void setConfigFile(File configurationFile) { configFile = configurationFile; }

	private void setConfigFilePath(File configurationFile) { configFilePath = configurationFile.getAbsolutePath(); }

	/**
	 * Returns the configuration file found on instantiation's file object.
	 * @return File the configuration file object.
	 */
	public File getConfigFile() { return configFile; }

	/**
	 * Returns the configuration file found on instantiation's absolute path as a string.
	 * @return String the absolute path of the configuration file loaded.
	 */
	public String getConfigFilePath() { return configFilePath; }
	
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