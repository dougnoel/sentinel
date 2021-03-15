package com.dougnoel.sentinel.configurations;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.dougnoel.sentinel.exceptions.AccessDeniedException;
import com.dougnoel.sentinel.exceptions.ConfigurationMappingException;
import com.dougnoel.sentinel.exceptions.ConfigurationNotFoundException;
import com.dougnoel.sentinel.exceptions.ConfigurationParseException;
import com.dougnoel.sentinel.exceptions.FileNotFoundException;
import com.dougnoel.sentinel.exceptions.IOException;
import com.dougnoel.sentinel.exceptions.PageNotFoundException;
import com.dougnoel.sentinel.exceptions.PageObjectNotFoundException;
import com.dougnoel.sentinel.exceptions.URLNotFoundException;
import com.dougnoel.sentinel.pages.PageData;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 *  Manages configuration actions, changes, and interaction with PageObject including functionality to get default timeout,
 *  default time units, environment data, config path and package lists from PageObject, current URL or URL from a given pageName,
 *  user name or password from given account, env, or page, loads pageData for a given pageName, authentication with SSL, 
 *  searching directories, and class getter and setter method.
 *  
 */
public class ConfigurationManager {
	private static final Logger log = LogManager.getLogger(ConfigurationManager.class); // Create a logger.

	private static final Map<String,PageData> PAGE_DATA = new ConcurrentHashMap<>();
	private static ConfigurationManager instance = null;
	
	private static String env = null;
	
	private static Properties appProps = new Properties();
	
	private static ConfigurationData sentinelConfigurations = null;
	
	private static final String CONFIG_FILEPATH = "conf/sentinel.yml";
	private static final String DEFAULT = "default";
    private static final String LINUX = "linux";
    private static final String MAC = "mac";
    private static final String WINDOWS = "windows";
    private static final String BROWSER = "browser";

	private ConfigurationManager() {
		// Exists only to defeat instantiation.
	}
	/**
	 * Returns instance of ConfigurationManager
	 * 
	 * @return ConfigurationManager the instance of this class
	 */
	public static ConfigurationManager getInstance() {
		if (instance == null) {
			instance = new ConfigurationManager();
		}
		return instance;
	}
	
	/**
	 * Returns the configuration value for the given configuration property and the given environment from the ConfigurationData class.
	 * 
	 * @param configurationKey String the key for the requested configuration property
	 * @return String the configuration value
	 */
	private static String getOrCreateConfigurationData(String configurationKey) {	
		//First we see if the property is set on the maven commandline or in code.
		String data = System.getProperty(configurationKey);
		if (data != null) {
			return data;
		}
		
		if(sentinelConfigurations == null) {
			try {
				ObjectMapper mapper = new ObjectMapper(new YAMLFactory()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				sentinelConfigurations = mapper.readValue( new ConfigurationData(), ConfigurationData.class );
			} catch (JsonParseException e) {
				String errorMessage = SentinelStringUtils.format("Configuration file {} is not a valid YAML file. Could not load the {} property. Please fix the file or pass the property in on the commandline using the -D{}= option.", CONFIG_FILEPATH, configurationKey, configurationKey);
				throw new ConfigurationParseException(errorMessage, e);
			} catch (JsonMappingException e) {
				String errorMessage = SentinelStringUtils.format("Configuration file {} has incorrect formatting and cannot be read. Could not load the {} property. Please fix the file or pass the property in on the commandline using the -D{}= option.", CONFIG_FILEPATH, configurationKey, configurationKey);
				throw new ConfigurationMappingException(errorMessage, e);
			} catch (java.io.FileNotFoundException e) {
				String errorMessage = SentinelStringUtils.format("Configuration file {} cannot be found in the specified location. Could not load the {} property. Please fix the file or pass the property in on the commandline using the -D{}= option.", CONFIG_FILEPATH, configurationKey, configurationKey);
				throw new FileNotFoundException(errorMessage, e);
			} catch (java.io.IOException e) {
				String errorMessage = SentinelStringUtils.format("Configuration file {} cannot be opened in the specified location. Could not load the {} property. Please fix the file read properties or pass the property in on the commandline using the -D{}= option.", CONFIG_FILEPATH, configurationKey, configurationKey);
				throw new IOException(errorMessage, e);
			}
		}
	 
		return sentinelConfigurations.getConfigurationValue(getEnvironment(), configurationKey);	
	}
	
	/**
	 * Returns the configuration property for the given key from the helper function which interfaces with the
	 * ConfigurationData object if it exists. If it does not exist, it is created by the helper function. If
	 * the value is not found anywhere, the ConfigurationNotFound error is suppressed and a null is returned
	 * instead of halting the progress of the program. If the configuration file is not found, we likewise
	 * suppress the FileNotFoundException and return null.
	 * 
	 * @param property String the requested configuration property key
	 * @return String the requested configuration property key or null if nothing is found
	 */
	public static String getOptionalProperty(String property) {
		try {
			String systemProperty = System.getProperty(property);
			if(systemProperty == null) {
				systemProperty = getOrCreateConfigurationData(property);
			}		
			return systemProperty;
		} catch (ConfigurationNotFoundException e) {
			log.trace(e.getMessage(),Arrays.toString(e.getStackTrace()));
			return null; //If the configuration value is not found, we do not need to throw an exception.
		}
	}
	
	/**
	 * Returns the configuration property for the given key from the helper function which interfaces with the
	 * ConfigurationData object if it exists. If it does not exist, it is created by the helper function.
	 * 
	 * @see ConfigurationManager#getOrCreateConfigurationData(String)
	 * @param property String the requested configuration property key
	 * @return String the configuration property value
	 */
	public static String getProperty(String property)  {
		String systemProperty = System.getProperty(property);
		if(systemProperty == null) {
			try {
				systemProperty = getOrCreateConfigurationData(property);
			} catch (ConfigurationNotFoundException e) {
				log.error(e.getMessage(),Arrays.toString(e.getStackTrace()));
				throw e;
			}
		}
		//Checked a second time, because this option is assumed to not be optional.
		//TODO: Rename this method to getRequiredProperty and have it call getOptionalProperty to reduce the number of checks we make.
		if(systemProperty == null) {
			String errorMessage = ConfigurationManager.getConfigurationNotFoundErrorMessage(property);
			log.error(errorMessage);
			throw new ConfigurationNotFoundException(errorMessage);
		}
		return systemProperty;
	}

	/**
	 * Returns the system environment.
	 * If no environment is set, a warning message is logged and a default value of "localhost" is set.
	 * 
	 * @return String text of system env info
	 */
	public static String getEnvironment() {
		if (env == null) {
			env = System.getProperty("env");
			if (env == null) {
				env = "localhost";
				String warningMessage = "localhost env being used by default. " + 
						ConfigurationManager.getConfigurationNotFoundErrorMessage("env");
				log.warn(warningMessage);
			}
		}
		return env;
	}
	
	/**
	 * Setter intended only for unit testing. Sets the stored value and also the System Property.
	 * @param env String env to set, null to clear
	 */
	protected static void setEnvironment(String env) {
		ConfigurationManager.env = env;
		if (env == null)
			System.clearProperty("env");
		else
			System.setProperty("env", env);
	}

	/**
	 * Returns the YAML config file path in the project for a given page object.
	 * 
	 * @param pageName String the name of the page object
	 * @return File the OS path to the config file
	 */
	public static File getPageObjectConfigPath(String pageName)  {
		String filename = pageName + ".yml";
		File result = searchDirectory(new File("src/"), filename);

		if (result == null) {
			String errorMessage = SentinelStringUtils.format("Failed to locate the {} configuration file. Please ensure the file exists in the same directory as the page object.", filename);
			log.error(errorMessage);
			throw new FileNotFoundException(filename);
		}

		return result;
	}

	/**
	 * Returns a File handler to a file if it is found in the given directory or any
	 * sub-directories.
	 * 
	 * @param directory File the directory to start the search
	 * @param fileName String the full name of the file with extension to find
	 * @return File the file that is found, null if nothing is found
	 */
	public static File searchDirectory(File directory, String fileName) {
		log.trace("Searching directory {}", directory.getAbsoluteFile());
		File searchResult = null;
		if (directory.canRead()) {
			for (File temp : directory.listFiles()) {
				if (temp.isDirectory()) {
					searchResult = searchDirectory(temp, fileName);
				} else {
					if (fileName.equals(temp.getName()))
						searchResult = temp.getAbsoluteFile();
				}
				if (searchResult != null) {
					break;
				}
			}
		} else {
			throw new AccessDeniedException(directory.getAbsoluteFile().toString());
		}
		return searchResult;
	}
  
	/**
	 * Returns page data through yaml instructions to a config path in given pageName string. 
	 * 
	 * @see com.dougnoel.sentinel.configurations.ConfigurationManager#getPageObjectConfigPath(String)
	 * @param pageName String the name of the page for which the data is retrieved
	 * @return PageData the class for the data on desired page
	 * @throws ConfigurationNotFoundException if a configuration option cannot be loaded
	 * @throws PageObjectNotFoundException if the page object file could not be read
	 */
	private static PageData loadPageData(String pageName) {
		PageData pageData = null;
		try {
			pageData = PageData.loadYaml(getPageObjectConfigPath(pageName));
		} catch (java.nio.file.AccessDeniedException e) {
			String errorMessage = SentinelStringUtils.format("Could not access the file {}.yml. Please ensure the file can be read by the current user and is not password protected.", pageName);
			log.error(errorMessage);
			throw new PageObjectNotFoundException(errorMessage, e);
		} catch (java.io.IOException e) {
			String errorMessage = SentinelStringUtils.format("Could not access the file {}.yml. Please ensure the file exists and the the pageObjectPackages value is set to include its package.", pageName);
			log.error(errorMessage);
			throw new PageObjectNotFoundException(errorMessage, e);
		}
		if (pageData == null) {
			String errorMessage = SentinelStringUtils.format("The file {}.yml appears to contain no data. Please ensure the file is properly formatted", pageName);
			log.error(errorMessage);
			throw new PageObjectNotFoundException(errorMessage);
		}
		log.trace("Page data loaded: {}", pageName);
		return pageData;
	}

	/**
	 * Returns the URL for the currently active page based on the environment value set. 
	 * 
	 * @see com.dougnoel.sentinel.pages.PageManager#getPage()
	 * @see com.dougnoel.sentinel.pages.Page#getName()
	 * @return String the desired URL
	 * @throws PageNotFoundException if page is not found
	 * @throws URLNotFoundException if url is not found for the page
	 */
	public static String getUrl()  {
		return getUrl(PageManager.getPage().getName());
	}
	
	/**
	 * Returns a URL for the given page name based on the environment value set.
	 
	 * @param pageName String the name of the page from which the url is retrieved
	 * @return String baseUrl the url for the given page and current environment
	 */
	public static String getUrl(String pageName) {
		String baseURL = null;
		PageData pageData = loadPageData(pageName);
		String env = ConfigurationManager.getEnvironment();

		if (pageData.containsUrl(env)) {
			baseURL = pageData.getUrl(env);
		} else if (pageData.containsUrl(DEFAULT)){
			baseURL = pageData.getUrl(DEFAULT);
			baseURL = StringUtils.replace(baseURL, "{env}", env);
		} else if (pageData.containsUrl("base")){
			baseURL = pageData.getUrl("base");
			baseURL = StringUtils.replace(baseURL, "{env}", env);
		}
		if (StringUtils.isEmpty(baseURL)) {
			String errorMessage = SentinelStringUtils.format("A url was not found for the {} environment in your {}.yml file. Please add a URL to the yml file. See the project README for details.", env, pageName);
			log.error(errorMessage);
			throw new URLNotFoundException(errorMessage);
		}
		return baseURL;
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
	public static String getAccountInformation(String account, String key) {
		String pageName = PageManager.getPage().getName();
		String env = getEnvironment();
		PageData pageData = loadPageData(pageName);
		Map <String,String> accountData = pageData.getAccount(env, account);
		if (Objects.equals(accountData, null)) {
			env = DEFAULT;
			accountData = pageData.getAccount(env, account);
		}
		if (Objects.equals(accountData, null)) {
			String erroMessage = SentinelStringUtils.format("Account {} could not be found for the {} environment in {}.yml", account, env, pageName);
			log.debug(erroMessage);
			throw new ConfigurationNotFoundException(erroMessage);
		}
		String data = accountData.get(key);
		log.debug("{} loaded for account {} in {} environment from {}.yml: {}", key, account, env, pageName, data);
		return data;
	}
	
	public static Map <String,String> getElement(String elementName, String pageName) {
		return PAGE_DATA.computeIfAbsent(pageName, ConfigurationManager::loadPageData).getElement(elementName);
	}

	public static String[] getPageParts(String pageName) {
		return PAGE_DATA.computeIfAbsent(pageName, ConfigurationManager::loadPageData).getPageParts();
	}
	
	/**
	 * Stores values in a property object for quick and dirty dependency injection.
	 * Replaces space chars with '_' char, makes key all lowercase, and logs action.
	 * 
	 * @param key String the key to set
	 * @param value String the value to set
	 */
	public static void setValue(String key, String value) {
		key = key.replaceAll("\\s+", "_").toLowerCase();
		appProps.setProperty(key, value);
		log.trace("Stored key/value pair: {}/{}", key, value);
	}

	/**
	 * Retrieves values between steps during quick and dirty dependency injection.
	 * Replaces space chars with '_' char, makes key all lowercase, and logs action.
	 *
	 * @param key String the item to get 
	 * @return String the value for the given key
	 */
	public static String getValue(String key) {
		key = key.replaceAll("\\s+", "_").toLowerCase();
		String value = appProps.getProperty(key);
		log.trace("Retrieved key/value pair: {}/{}", key, value);
		return value;
	}
	
	public static String getConfigurationNotFoundErrorMessage(String configurtaionValue) {
		return SentinelStringUtils.format("No {} property set. This can be set in the sentinel.yml config file with a '{}=' property or on the command line with the switch '-D{}='.", configurtaionValue, configurtaionValue, configurtaionValue);
	}
	
    /**
     * Returns a sanitized version of the browser set in the config file or on the command line.
     * @return String a sanitized string containing the browser
     */
    public static String getBrowserName() {
    	//TODO Make this useable by Saucelabs driver
    	String browser;
		browser = ConfigurationManager.getOptionalProperty(BROWSER);
		if (browser == null) {
			browser = "chrome";
			String infoMessage = "Chrome browser being used by default. " + 
					ConfigurationManager.getConfigurationNotFoundErrorMessage(BROWSER);
			log.info(infoMessage);
		}
        // Make sure whatever string we are passed is all lower case and all spaces are removed.
        browser = browser.replaceAll("\\s+", "").toLowerCase();
        if (browser.equals("ie"))
            browser = "internetexplorer";
        return browser;
    }
    
    /**
     * Returns a sanitized version of the operating system set in the config file or on the command line.
     * @return String a sanitized string containing the operating system
     */
    public static String getOperatingSystem() {
    	String operatingSystem = ConfigurationManager.getOptionalProperty("os");
    	if (operatingSystem == null) {
    		operatingSystem = detectOperatingSystem();
    	}
        
        return operatingSystem;
    }
    
    /**
     * Returns the simple operating system of windows, mac or linux. Returns the full os name if none of
     * those are matched.
     * @return String operating system
     */
    private static String detectOperatingSystem() {
    	String os = System.getProperty("os.name").toLowerCase();
		String infoMessage = SentinelStringUtils.format("Operating system auto-detected: {} ", os) + 
				ConfigurationManager.getConfigurationNotFoundErrorMessage("os");
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
