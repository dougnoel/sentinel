package com.dougnoel.sentinel.configurations;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.exceptions.FileException;
import com.dougnoel.sentinel.pages.PageData;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.system.FileManager;
import com.dougnoel.sentinel.system.TestManager;
import com.dougnoel.sentinel.system.YAMLObject;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 *  Manages configuration actions, changes, and interaction with PageObject including functionality to get default timeout,
 *  default time units, environment data, config path and package lists from PageObject, current URL or URL from a given pageName,
 *  user name or password from given account, env, or page, loads pageData for a given pageName, authentication with SSL, 
 *  searching directories, and class getter and setter method.
 *  
 */
public class Configuration {
	private static final Logger log = LogManager.getLogger(Configuration.class);

	private static final Map<String,PageData> PAGE_DATA = new ConcurrentHashMap<>();
	private static final Map<String,YAMLData> YAML_DATA = new ConcurrentHashMap<>();
	
	private static final String ENV_REPLACE_STRING = "{env}";
	private static final String ENV = "env";

	private static final Properties appProps = new Properties();
	
	private static ConfigurationData sentinelConfigurations = null;
	
	private static final File CONFIGURATION_FILE = new File("conf/sentinel.yml");
	private static final String DEFAULT = "default";
    private static final String LINUX = "linux";
    private static final String MAC = "mac";
    private static final String WINDOWS = "windows";
    private static final String BROWSER = "browser";

    /**
     * Exists only to defeat instantiation.
     */
	private Configuration() {
	}
	
	/**
	 * Returns the configuration value for the given configuration property and the given environment from 
	 * the ConfigurationData class.
	 * 
	 * @param configurationKey String the key for the requested configuration property
	 * @return String the configuration value
	 */
	private static String getOrCreateConfigurationData(String configurationKey) {
		String data = System.getProperty(configurationKey);
		if (data != null) {
			return data;
		}
		
		if(sentinelConfigurations == null) {
			try {
				ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
						.configure(DeserializationFeature
						.FAIL_ON_UNKNOWN_PROPERTIES, false);
				sentinelConfigurations = mapper.readValue( new ConfigurationData(), ConfigurationData.class );
			} catch (Exception e) {
				String errorMessage = SentinelStringUtils.format("Could not load the {} property because of the exception: {}." + System.lineSeparator() +
						"Please fix the file or pass the property in on the commandline using the -D{}= option.", configurationKey, e.getMessage(), configurationKey, configurationKey);
				throw new FileException(errorMessage, e, CONFIGURATION_FILE);
			}
		}

		return sentinelConfigurations.getConfigurationValue(environment(), configurationKey);
	}
	
	/**
	 * Returns the configuration for a given property as a String value. It has the following precedence
	 * for searching for a value:
	 * <ol>
	 * <li>A value stored at runtime.</li>
	 * <li>A value set on the command line.</li>
	 * <li>A value set in the configuration file.</li>
	 * </ol>
	 * The first time it is run, the value found will be stored for future calls. If no value is found,
	 * null is returned.
	 * 
	 * @param property String the requested configuration property
	 * @return String the value of the requested configuration property (null if nothing is found)
	 */
	public static String toString(String property) {
		String propertyValue = appProps.getProperty(property);
		
		if(propertyValue == null)
			propertyValue = System.getProperty(property);

		if(propertyValue == null) {
			try {
				if (property.contentEquals(ENV)) {
					String warningMessage = "Configuration value 'env' not found in runtime or command-line configuration. Default value will be used.";
					log.warn(warningMessage);
					return null;
				}
				propertyValue = getOrCreateConfigurationData(property);
			} catch (FileException e) {
				log.trace(e.getMessage(), Arrays.toString(e.getStackTrace()));
				return null;
			}
		}

		if(propertyValue != null)
			appProps.setProperty(property, propertyValue);
		return propertyValue;
	}
	
	/**
	 * Returns the configuration for a given property as a String value. It has the following precedence
	 * for searching for a value:
	 * <ol>
	 * <li>A value stored at runtime.</li>
	 * <li>A value set on the command line.</li>
	 * <li>A value set in the configuration file.</li>
	 * </ol>
	 * The first time it is run, the value found will be stored for future calls. If no value is found,
	 * the passed default value is returned.
	 * 
	 * @param property String the requested configuration property
	 * @param defaultValue String the default to return if no value is found
	 * @return String the value of the requested configuration property (null if nothing is found)
	 */
	public static String toString(String property, String defaultValue) {
		String propertyValue = toString(property);
		if (propertyValue == null) {
			appProps.setProperty(property, defaultValue);
			String warningMessage = SentinelStringUtils.format("{} being used by default for configuration property {}. {}",
						defaultValue, property, Configuration.configurationNotFoundErrorMessage(property));
			log.warn(warningMessage);

			return defaultValue;
		}
		return propertyValue; 
	}
	
	/**
	 * Updates a configuration value once runtime has started. This should never be used in a Cucumber runner
	 * as it will mask any values in the configuration file and on the command line.
	 * 
	 * @param property String the property to update
	 * @param value String the value to be used
	 */
	public static void update(String property, String value) {
		appProps.setProperty(property, value);
	}

	/**
	 * Returns a Set&lt;String&gt; containing all property (key) names in the Configuration's stored Properties (appProps) that start with the given prefix.
	 * Case-sensitive.
	 * The Properties object that this method searches (appProps) does not always include every configuration variable.
	 * Entries are only added to the Properties if/when they are read from other sources, or when entries are added during the course of a test.
	 * As a result, the set returned by this method actually contains all configuration entries that start with the given prefix
	 * and have been read or updated since the start of the test, or the last reset of the session appProps (Properties).
	 * @param prefix String the prefix to filter properties by. Any entry in the Properties that starts with the given prefix (or exactly matches it) will be returned in the Set.
	 * @return Set&lt;String&gt; containing all property (key) names in the Configuration's stored Properties that start with the given prefix.
	 */
	public static Set<String> getAllPropertiesWithPrefix(String prefix) {
		return appProps.stringPropertyNames().stream().filter(property -> property.startsWith(prefix)).collect(Collectors.toSet());
	}

	/**
	 * Returns the number of properties in the Configuration's stored Properties (appProps) that start with the given prefix.
	 * Case-sensitive.
	 * The Properties object that this method searches (appProps) does not always include every configuration variable.
	 * Entries are only added to the Properties if/when they are read from other sources, or when entries are added during the course of a test.
	 * As a result, the number returned by this method actually counts all configuration entries that start with the given prefix
	 * and have been read or updated since the start of the test, or the last reset of the session appProps (Properties).
	 * @param prefix String the prefix to filter properties by. Any entry in appProps that starts with the given prefix (or exactly matches it) will be counted.
	 * @return long the number of properties that start with the given prefix.
	 */
	public static long getNumberOfPropertiesWithPrefix(String prefix) {
		return getAllPropertiesWithPrefix(prefix).size();
	}
	
	/**
	 * Clears a configuration value once runtime has started. This should never be used in a Cucumber runner
	 * as it will mask any values in the configuration file and on the command line. 
	 * 
	 * @param property String the property to clear
	 */
	public static void clear(String property) {
		appProps.remove(property);
	}

	/**
	 * Clears all configuration values that have been set since runtime started.
	 */
	public static void clearAllSessionAppProps() { appProps.clear(); }
	
	/**
	 * Returns the given configuration value stored in the passed property as a Double, or 0.0 if nothing is
	 * found.
	 * 
	 * @param property String the requested configuration property key
	 * @return Double the requested value as a Double or 0.0 if nothing valid is found
	 */
	public static Double toDouble(String property) {
		try {
			return Double.valueOf(toString(property));
		} catch (Exception e) {
			log.trace(e.getMessage(),Arrays.toString(e.getStackTrace()));
			return 0.0;
		}
	}

	/**
	 * Returns the given configuration value stored in the passed property as a boolean, or false if nothing is
	 * found.
	 * 
	 * @param property String the requested configuration property key
	 * @return boolean the requested value as a boolean or false if nothing valid is found
	 */
	public static boolean toBoolean(String property) {
		String prop = toString(property);
		if(prop != null) {
			switch(prop.toLowerCase()) {
				case "true":
				case "":
					return true;
				default:
					return false;
			}
		}
		else {
			return false;
		}
	}

	/**
	 * Updates a configuration value once runtime has started. This should never be used in a Cucumber runner
	 * as it will mask any values in the configuration file and on the command line.
	 * 
	 * @param property String the property to update
	 * @param value Double the value to be used
	 */
	public static void update(String property, double value) {
		update(property, String.valueOf(value));
	}
	
	/**
	 * Returns the given configuration value stored in the passed property as a Long, or 0L if nothing is
	 * found.
	 * 
	 * @param property String the requested configuration property key
	 * @return Double the requested value as a Double or 0.0 if nothing valid is found
	 */
	public static Long toLong(String property) {
		try {
			return Long.valueOf(toString(property));
		} catch (Exception e) {
			log.trace(e.getMessage(),Arrays.toString(e.getStackTrace()));
			return 0L;
		}
	}

	/**
	 * Updates a configuration value once runtime has started. This should never be used in a Cucumber runner
	 * as it will mask any values in the configuration file and on the command line.
	 * 
	 * @param property String the property to update
	 * @param value Long the value to be used
	 */
	public static void update(String property, long value) {
		update(property, String.valueOf(value));
	}
	
	/**
	 * Returns the system environment.
	 * If no environment is set, a warning message is logged and a default value of "localhost" is set.
	 * 
	 * @return String text of system env info
	 */
	public static String environment() {
		return toString(ENV, "localhost");
	}

	/**
	 * Returns the file path for a given page object.
	 * 
	 * @param pageName String the name of the page object
	 * @return File the OS path to the config file
	 */
	public static File findPageObjectFilePath(String pageName) {
		String fileName = pageName + ".yml";
		return FileManager.findFilePath(fileName);
	}

	/**
	 * Returns page data through yaml instructions to a config path in given pageName string. 
	 * 
	 * @param pageName String the name of the page for which the data is retrieved
	 * @return PageData the data from the configuration file on disk
	 */
	private static PageData loadPageData(String pageName) {
		PageData pageData = null;
		try {
			pageData = PageData.loadYaml(findPageObjectFilePath(pageName));
		} catch (Exception e) {
			var errorMessage = SentinelStringUtils.format("Could not load the {}.yml page object.", pageName);
			throw new FileException(errorMessage, e, new File(pageName + ".yml"));
		}

		if (pageData == null) {
			var errorMessage = "The file appears to contain no data. Please ensure the file is properly formatted.";
			throw new FileException(errorMessage, new File(pageName + ".yml"));
		}
		
		return pageData;
	}
	
	/**
	 * Returns data from yaml file.
	 * 
	 * @param yamlName String the name of the object for data retrieval
	 * @return YAMLData the data from the configuration file on disk
	 */
	private static YAMLData loadYAMLData(String yamlName) {
		YAMLData yamlData = null;
		try {
			yamlData = YAMLData.loadYaml(findPageObjectFilePath(yamlName));
		} catch (Exception e) {
			var errorMessage = SentinelStringUtils.format("Could not load the {}.yml page object.", yamlName);
			throw new FileException(errorMessage, e, new File(yamlName + ".yml"));
		}

		if (yamlData == null) {
			var errorMessage = "The file appears to contain no data. Please ensure the file is properly formatted.";
			throw new FileException(errorMessage, new File(yamlName + ".yml"));
		}
		
		return yamlData;
	}
	
	/**
	 * Returns yamlData for the given yamlObject by determining what type it is and calling
	 * the appropriate method. If the object has already been loaded from disk, it is returned,
	 * otherwise it is loaded from disk then returned.
	 * @param yamlObject YAMLObject the API/Page object to retrieve data for
	 * @return YAMLData the data from the API/Page configuration file
	 */
	private static YAMLData getYAMLData(YAMLObject yamlObject) {
		String yamlName = yamlObject.getName();
		switch (yamlObject.getType()) {
		case PAGE:
			return PAGE_DATA.computeIfAbsent(yamlName, Configuration::loadPageData);
		case API:
		case UNKNOWN:
		default:
			return YAML_DATA.computeIfAbsent(yamlName, Configuration::loadYAMLData);
		}
	}
	
	/**
	 * Returns the type of page object. Most will be WEBPAGE, but if an "executables:"
	 * section is defined instead of a "urls:" section, this will return EXECUTABLE as the type.
	 * If we do not know what the current type is, we infer it by using the current type set in
	 * the PageManager.
	 * 
	 * @param pageName String the name of the page for which the data is retrieved
	 * @return PageObjectType the type of page object either WEBPAGE or EXECUTABLE
	 */
	public static PageObjectType getPageObjectType(String pageName) {
		var pageData = loadPageData(pageName);
		if (pageData.hasUrls())
			return PageObjectType.WEBPAGE;
		if (pageData.hasExecutables())
			return PageObjectType.EXECUTABLE;

		return PageManager.getCurrentPageObjectType();
	}
	
	/**
	 * 
	 * @param yamlObject YAMLObject the YAML Object (API/Page object) to retrieve the URL from
	 * @return String the url for the given yaml object and current environment
	 */
	public static String getURL(YAMLObject yamlObject) {
		String baseURL = null;
		var yamlData = getYAMLData(yamlObject);
		var yamlName = yamlObject.getName();
		String env = Configuration.environment();

		if (yamlData.containsUrl(env)) {
			baseURL = yamlData.getUrl(env);
		} else if (yamlData.containsUrl(DEFAULT)){
			baseURL = yamlData.getUrl(DEFAULT);
			baseURL = StringUtils.replace(baseURL, ENV_REPLACE_STRING, env);
		} else if (yamlData.containsUrl("base")){
			baseURL = yamlData.getUrl("base");
			baseURL = StringUtils.replace(baseURL, ENV_REPLACE_STRING, env);
		}
		if (StringUtils.isEmpty(baseURL)) {
			var errorMessage = SentinelStringUtils.format("A url was not found for the {} environment in your {}.yml file. Please add a URL to the page object. See the project README for details.", env, yamlName);
			throw new FileException(errorMessage, new File(yamlName + ".yml"));
		}
		return baseURL;
	}
	
	/**
	 * Returns the Executable for the currently active page based on the environment value set. 
	 * 
	 * @return String the desired application executable path
	 */
	public static String executable() {
		return executable(PageManager.getPage().getName());
	}
	
	/**
	 * Returns an Executable path for the given page name based on the environment value set.
	 
	 * @param pageName String the name of the page from which the executable is retrieved
	 * @return String the executable path for the given page and current environment
	 */
	protected static String executable(String pageName) {
		String executablePath = null;
		var pageData = loadPageData(pageName);
		String env = Configuration.environment();

		if (pageData.containsExecutable(env)) {
			executablePath = pageData.getExecutable(env);
		} else if (pageData.containsExecutable(DEFAULT)){
			executablePath = pageData.getExecutable(DEFAULT);
			executablePath = StringUtils.replace(executablePath, ENV_REPLACE_STRING, env);
		}
		if (StringUtils.isEmpty(executablePath)) {
			var errorMessage = SentinelStringUtils.format("An executable was not found for the {} environment in your {}.yml file. Please add an executable to the yml file. See the project README for details.", env, pageName);
			throw new FileException(errorMessage, new File(pageName + ".yml"));
		}
		return executablePath;
	}
	
	/**
	 * Returns username or password. Parent Method.
	 * 
	 * Creates pageData object and gets user name or password from its account and env. Logs the key from account info.
	 * and logs data var before returning.
	 * 
	 * @param account String user account
	 * @param key String username or password
	 * @return String requested username or password
	 */
	public static String accountInformation(String account, String key) {
		String pageName = TestManager.getActiveTestObject().getName();
		String env = environment();
		var pageData = loadPageData(pageName);
		Map <String,String> accountData = pageData.getAccount(env, account);
		if (Objects.equals(accountData, null)) {
			env = DEFAULT;
			accountData = pageData.getAccount(env, account);
		}
		if (Objects.equals(accountData, null)) {
			var errorMessage = SentinelStringUtils.format("Account {} could not be found for the {} environment in {}.yml", account, env, pageName);
			throw new FileException(errorMessage, new File(pageName + ".yml"));
		}
		String data = accountData.get(key);
		log.debug("{} loaded for account {} in {} environment from {}.yml: {}", key, account, env, pageName, data);
		return data;
	}
	
	/**
	 * Returns an element if found in the PAGE_DATA store for the passed page name. If it is not found
	 * the yaml file is searched and it is created in the PAGE_DATA store before being returned.
	 * 
	 * @param elementName the element to search for in the page object
	 * @param pageName the name of the page object to search
	 * @return the element and all of its locators
	 */
	public static Map <String,String> getElement(String elementName, String pageName) {
		return PAGE_DATA.computeIfAbsent(pageName, Configuration::loadPageData).getElement(elementName);
	}
	
	/**
	 * Returns the value of the given key of the given testdata object in the current environment. 
	 * Defaults to "default" if environment is not set.
	 * <p>Example of testdata:</p>
	 * <pre>
	 * testdata:
	 *   default:
	 *     report:
	 *       id: 09876
	 *       version: 9
	 *   alpha:
	 *     report:
	 *       id: 123456
	 *       version: 1
	 * </pre>
	 * <p>To retrieve the version of report, call <b>getTestdataValue("report", "version")</b></p>
	 * <p>Example 2 of testdata:</p>
	 * <pre>2
	 * testdata:
	 *   default:
	 *     puppydata:
	 *       json: |
	 *        {
	 *          "id": 10,
	 *          "name": "doggie",
	 *          "category": {
	 *            "id": 1,
	 *            "name": "Dogs"
	 *          },
	 *          "photoUrls": [
	 *            "string"
	 *          ],
	 *          "tags": [
	 *            {
	 *              "id": 0,
	 *              "name": "string"
	 *            }
	 *          ],
	 *          "status": "available"
	 *        }
	 * </pre>
	 * <p>To retrieve the json for puppydata, call <b>getTestdataValue("puppydata", "json")</b></p>
	 * @param testDataObjectName String name of the testdata object
	 * @param testDataObjectKey String name of the property of the given testdata object
	 * @return String the value of the given key in the given object
	 */
	public static String getTestData(String testDataObjectName, String testDataObjectKey) {
		YAMLData yamlData = getYAMLData(TestManager.getActiveTestObject());
		return getTestData(yamlData, testDataObjectName, testDataObjectKey);
	}
	
	/**
	 * Helper method to get testdata from API and Page objects.
	 * @param yamlData YAMLData the data file to look in
	 * @param testDataObjectName String name of the testdata object
	 * @param testDataObjectKey String name of the property of the given testdata object
	 * @return String the value of the given key in the given object
	 */
	private static String getTestData(YAMLData yamlData, String testDataObjectName, String testDataObjectKey) {
		String normalizedTestdataObjectName = testDataObjectName.replaceAll("\\s+", "_");
		String normalizedTestdataObjectKey = testDataObjectKey.replaceAll("\\s+", "_");
		String env = environment();
		
		Map <String,String> testdata = yamlData.getTestdata(env, normalizedTestdataObjectName);
		if (testdata == null || testdata.isEmpty()) {
			env = DEFAULT;
			testdata = yamlData.getTestdata(env, normalizedTestdataObjectName);
		}
		if (testdata == null || testdata.isEmpty()) {
			var yamlName = yamlData.getName();
			var errorMessage = SentinelStringUtils.format("Testdata {} could not be found for the {} environment in {}.yml", normalizedTestdataObjectName, env, yamlName);
			throw new FileException(errorMessage, new File(yamlData.getName() + ".yml"));
		}
		String data = testdata.get(normalizedTestdataObjectKey);
		if (data == null) {
			var yamlName = yamlData.getName();
			var errorMessage = SentinelStringUtils.format("Data for {} key could not be found in {} for the {} environment in {}.yml", testDataObjectKey, normalizedTestdataObjectName, env, yamlName);
			throw new FileException(errorMessage, new File(yamlData + ".yml"));
		}
		log.debug("{} loaded for testdata object {} in {} environment from {}.yml: {}", normalizedTestdataObjectKey, normalizedTestdataObjectName, env, yamlData.getName(), data);
		return data;
	}
	
	/**
	 * Returns a String array containing all the included pages in a page object (if any).
	 * @param pageName String the page to check for includes
	 * @return String[] the pages found that are part of this page
	 */
	public static String[] getPageParts(String pageName) {
		return PAGE_DATA.computeIfAbsent(pageName, Configuration::loadPageData).getPageParts();
	}
	
	/**
	 * Returns a formatted error message. Helper method to reduce code duplication.
	 * @param configurtaionValue String the configuration that was not set
	 * @return String the formatted error message
	 */
	private static String configurationNotFoundErrorMessage(String configurtaionValue) {
		if(configurtaionValue.equalsIgnoreCase(ENV))
			return SentinelStringUtils.format("No {} property set. This can be set on the command line with the switch '-D{}='.",
					configurtaionValue);
		else
			return SentinelStringUtils.format("No {} property set. This can be set in the sentinel.yml config file with a '{}=' property or on the command line with the switch '-D{}='.",
					configurtaionValue, configurtaionValue, configurtaionValue);
	}
	
    /**
     * Returns a sanitized version of the browser set in the config file or on the command line.
     * @return String a sanitized string containing the browser
     */
    public static String browser() {
    	String browser;
		browser = Configuration.toString(BROWSER);
		if (browser == null) {
			browser = "chrome";
			appProps.setProperty(BROWSER, browser);
			String infoMessage = "Chrome browser being used by default. " + 
					Configuration.configurationNotFoundErrorMessage(BROWSER);
			log.info(infoMessage);
		}
		else // Make sure whatever string we are passed is all lower case and all spaces are removed.
			browser = browser.replaceAll("\\s+", "").toLowerCase();
        if (browser.equals("ie")) {
            browser = "internetexplorer";
            appProps.setProperty(BROWSER, browser);
        }
        return browser;
    }
    
    /**
     * Returns a sanitized version of the operating system set in the config file or on the command line.
     * @return String a sanitized string containing the operating system
     */
    public static String operatingSystem() {
    	var operatingSystem = Configuration.toString("os");
    	if (operatingSystem == null) {
    		operatingSystem = detectOperatingSystem();
    		appProps.setProperty("os", operatingSystem);
    	}
        
        return operatingSystem;
    }
    
    /**
     * Returns the simple operating system of windows, mac or linux. Returns the full os name if none of
     * those are matched.
     * @return String operating system
     */
    protected static String detectOperatingSystem() {
    	String os = System.getProperty("os.name").toLowerCase();
		String infoMessage = SentinelStringUtils.format("Operating system auto-detected: \"{}\". ", os) + 
				Configuration.configurationNotFoundErrorMessage("os");
    	log.info(infoMessage);
    	if (os.indexOf("win") >=0) {
    		return WINDOWS;
    	}
    	if (os.indexOf("mac") >= 0) {
    		return MAC;
    	}
    	if (os.indexOf("nux") >= 0) {
    		return LINUX;
    	}
    	return os;
    }
}