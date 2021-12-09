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

import com.dougnoel.sentinel.exceptions.ConfigurationMappingException;
import com.dougnoel.sentinel.exceptions.ConfigurationNotFoundException;
import com.dougnoel.sentinel.exceptions.ConfigurationParseException;
import com.dougnoel.sentinel.exceptions.FileNotFoundException;
import com.dougnoel.sentinel.exceptions.IOException;
import com.dougnoel.sentinel.exceptions.PageNotFoundException;
import com.dougnoel.sentinel.exceptions.PageObjectNotFoundException;
import com.dougnoel.sentinel.exceptions.URLNotFoundException;
import com.dougnoel.sentinel.filemanagers.FileManager;
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
public class Configuration {
	private static final Logger log = LogManager.getLogger(Configuration.class);

	private static final Map<String,PageData> PAGE_DATA = new ConcurrentHashMap<>();
	
	private static String env = null;
	
	private static Properties appProps = new Properties();
	
	private static ConfigurationData sentinelConfigurations = null;
	
	private static final String CONFIG_FILEPATH = "conf/sentinel.yml";
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
				ObjectMapper mapper = new ObjectMapper(new YAMLFactory()).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				sentinelConfigurations = mapper.readValue( new ConfigurationData(), ConfigurationData.class );
			} catch (JsonParseException e) {
				var errorMessage = SentinelStringUtils.format("Configuration file {} is not a valid YAML file. Could not load the {} property. Please fix the file or pass the property in on the commandline using the -D{}= option.", CONFIG_FILEPATH, configurationKey, configurationKey);
				throw new ConfigurationParseException(errorMessage, e);
			} catch (JsonMappingException e) {
				var errorMessage = SentinelStringUtils.format("Configuration file {} has incorrect formatting and cannot be read. Could not load the {} property. Please fix the file or pass the property in on the commandline using the -D{}= option.", CONFIG_FILEPATH, configurationKey, configurationKey);
				throw new ConfigurationMappingException(errorMessage, e);
			} catch (java.io.FileNotFoundException e) {
				var errorMessage = SentinelStringUtils.format("Configuration file {} cannot be found in the specified location. Could not load the {} property. Please fix the file or pass the property in on the commandline using the -D{}= option.", CONFIG_FILEPATH, configurationKey, configurationKey);
				throw new FileNotFoundException(errorMessage, e);
			} catch (java.io.IOException e) {
				var errorMessage = SentinelStringUtils.format("Configuration file {} cannot be opened in the specified location. Could not load the {} property. Please fix the file read properties or pass the property in on the commandline using the -D{}= option.", CONFIG_FILEPATH, configurationKey, configurationKey);
				throw new IOException(errorMessage, e);
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
		
		if(propertyValue == null) {
			try {
				propertyValue = System.getProperty(property);
				if(propertyValue == null) {
					propertyValue = getOrCreateConfigurationData(property);
				}
				else {
					appProps.setProperty(property, propertyValue);
				}
				return propertyValue;
			} catch (ConfigurationNotFoundException e) {
				log.trace(e.getMessage(),Arrays.toString(e.getStackTrace()));
				return null;
			}
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
	 * Clears a configuration value once runtime has started. This should never be used in a Cucumber runner
	 * as it will mask any values in the configuration file and on the command line. 
	 * 
	 * @param property String the property to clear
	 */
	public static void clear(String property) {
		appProps.remove(property);
	}
	
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
		if (env == null) {
			env = System.getProperty("env");
			if (env == null) {
				env = "localhost";
				String warningMessage = "localhost env being used by default. " + 
						Configuration.configurationNotFoundErrorMessage("env");
				log.warn(warningMessage);
			}
		}
		return env;
	}
	
	/**
	 * Setter intended only for unit testing. Sets the stored value and also the System Property.
	 * @param env String env to set, null to clear
	 */
	protected static void environment(String env) {
		Configuration.env = env;
		if (env == null)
			System.clearProperty("env");
		else
			System.setProperty("env", env);
	}

	/**
	 * Returns the file path for a given page object.
	 * 
	 * @param pageName String the name of the page object
	 * @return File the OS path to the config file
	 */
	private static File findPageObjectFilePath(String pageName)  {
		return new File(FileManager.findFilePath(pageName + ".yml"));
	}

	/**
	 * Returns a valid class path for instantiating a java class given a class name.
	 * 
	 * @param className String the name of the class (case sensitive)
	 * @return String the path to the class that can be used to create an object
	 */
	public static String getClassPath(String className) {
		String filePath = FileManager.findFilePath(className + ".java");
		String returnValue = StringUtils.removeEnd(filePath.replace("/", "."), ".java");
		return "com" + StringUtils.substringAfter(returnValue, "com");
	}

	/**
	 * Returns page data through yaml instructions to a config path in given pageName string. 
	 * 
	 * @see com.dougnoel.sentinel.configurations.Configuration#findPageObjectFilePath(String)
	 * @param pageName String the name of the page for which the data is retrieved
	 * @return PageData the class for the data on desired page
	 * @throws ConfigurationNotFoundException if a configuration option cannot be loaded
	 * @throws PageObjectNotFoundException if the page object file could not be read
	 */
	private static PageData loadPageData(String pageName) {
		PageData pageData = null;
		try {
			pageData = PageData.loadYaml(findPageObjectFilePath(pageName));
		} catch (java.nio.file.AccessDeniedException e) {
			var errorMessage = SentinelStringUtils.format("Could not access the file {}.yml. Please ensure the file can be read by the current user and is not password protected.", pageName);
			log.error(errorMessage);
			throw new PageObjectNotFoundException(errorMessage, e);
		} catch (java.io.IOException e) {
			var errorMessage = SentinelStringUtils.format("Could not access the file {}.yml. Please ensure the file exists and the the pageObjectPackages value is set to include its package.", pageName);
			log.error(errorMessage);
			throw new PageObjectNotFoundException(errorMessage, e);
		}
		if (pageData == null) {
			var errorMessage = SentinelStringUtils.format("The file {}.yml appears to contain no data. Please ensure the file is properly formatted", pageName);
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
	public static String url()  {
		return url(PageManager.getPage().getName());
	}
	
	/**
	 * Returns a URL for the given page name based on the environment value set.
	 
	 * @param pageName String the name of the page from which the url is retrieved
	 * @return String baseUrl the url for the given page and current environment
	 */
	protected static String url(String pageName) {
		String baseURL = null;
		var pageData = loadPageData(pageName);
		String env = Configuration.environment();

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
			var errorMessage = SentinelStringUtils.format("A url was not found for the {} environment in your {}.yml file. Please add a URL to the yml file. See the project README for details.", env, pageName);
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
	public static String accountInformation(String account, String key) {
		String pageName = PageManager.getPage().getName();
		String env = environment();
		var pageData = loadPageData(pageName);
		Map <String,String> accountData = pageData.getAccount(env, account);
		if (Objects.equals(accountData, null)) {
			env = DEFAULT;
			accountData = pageData.getAccount(env, account);
		}
		if (Objects.equals(accountData, null)) {
			var erroMessage = SentinelStringUtils.format("Account {} could not be found for the {} environment in {}.yml", account, env, pageName);
			log.debug(erroMessage);
			throw new ConfigurationNotFoundException(erroMessage);
		}
		String data = accountData.get(key);
		log.debug("{} loaded for account {} in {} environment from {}.yml: {}", key, account, env, pageName, data);
		return data;
	}
	
	public static Map <String,String> getElement(String elementName, String pageName) {
		return PAGE_DATA.computeIfAbsent(pageName, Configuration::loadPageData).getElement(elementName);
	}

	public static String[] getPageParts(String pageName) {
		return PAGE_DATA.computeIfAbsent(pageName, Configuration::loadPageData).getPageParts();
	}
	
	private static String configurationNotFoundErrorMessage(String configurtaionValue) {
		return SentinelStringUtils.format("No {} property set. This can be set in the sentinel.yml config file with a '{}=' property or on the command line with the switch '-D{}='.", configurtaionValue, configurtaionValue, configurtaionValue);
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
			String infoMessage = "Chrome browser being used by default. " + 
					Configuration.configurationNotFoundErrorMessage(BROWSER);
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
    public static String operatingSystem() {
    	var operatingSystem = Configuration.toString("os");
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
