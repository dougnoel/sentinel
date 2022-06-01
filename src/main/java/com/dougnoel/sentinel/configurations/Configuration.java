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

import com.dougnoel.sentinel.enums.PageObjectType;
import com.dougnoel.sentinel.exceptions.FileException;
import com.dougnoel.sentinel.pages.PageData;
import com.dougnoel.sentinel.pages.PageManager;
import com.dougnoel.sentinel.strings.SentinelStringUtils;
import com.dougnoel.sentinel.system.FileManager;
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
	
	private static final String ENV_REPLACE_STRING = "{env}";
	private static String env = null;
	
	private static Properties appProps = new Properties();
	
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
			} catch (FileException e) {
				log.trace(e.getMessage(),Arrays.toString(e.getStackTrace()));
				return null;
			}
		}
		
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
	public static File findPageObjectFilePath(String pageName)  {
		return FileManager.findFilePath(pageName + ".yml");
	}

	/**
	 * Returns page data through yaml instructions to a config path in given pageName string. 
	 * 
	 * @see com.dougnoel.sentinel.configurations.Configuration#findPageObjectFilePath(String)
	 * @param pageName String the name of the page for which the data is retrieved
	 * @return PageData the class for the data on desired page
	 */
	private static PageData loadPageData(String pageName) {
		PageData pageData = null;
		try {
			pageData = PageData.loadYaml(findPageObjectFilePath(pageName));
		} catch (Exception e) {
			if (e instanceof FileException)
				throw (FileException)e;
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
	 * Returns the URL for the currently active page based on the environment value set. 
	 * 
	 * @see com.dougnoel.sentinel.pages.PageManager#getPage()
	 * @see com.dougnoel.sentinel.pages.Page#getName()
	 * @return String the desired URL
	 */
	public static String url() {
		return url(PageManager.getPage().getName());
	}
	
	/**
	 * Returns a URL for the given page name based on the environment value set.
	 
	 * @param pageName String the name of the page from which the url is retrieved
	 * @return String the url for the given page and current environment
	 */
	protected static String url(String pageName) {
		String baseURL = null;
		var pageData = loadPageData(pageName);
		String env = Configuration.environment();

		if (pageData.containsUrl(env)) {
			baseURL = pageData.getUrl(env);
		} else if (pageData.containsUrl(DEFAULT)){
			baseURL = pageData.getUrl(DEFAULT);
			baseURL = StringUtils.replace(baseURL, ENV_REPLACE_STRING, env);
		} else if (pageData.containsUrl("base")){
			baseURL = pageData.getUrl("base");
			baseURL = StringUtils.replace(baseURL, ENV_REPLACE_STRING, env);
		}
		if (StringUtils.isEmpty(baseURL)) {
			var errorMessage = SentinelStringUtils.format("A url was not found for the {} environment in your {}.yml file. Please add a URL to the page object. See the project README for details.", env, pageName);
			throw new FileException(errorMessage, new File(pageName + ".yml"));
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
		String pageName = PageManager.getPage().getName();
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
	
	public static Map <String,String> getElement(String elementName, String pageName) {
		return PAGE_DATA.computeIfAbsent(pageName, Configuration::loadPageData).getElement(elementName);
	}
	
	/**
	 * Gets the value of the given key of the given testdata object in the current environment. Defaults to "default" if environment is not set.
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
	 * @param testdataObjectName String name of the testdata object
	 * @param testdataObjectKey String name of the property of the given testdata object
	 * @return String the value of the given key in the given object
	 */
	public static String getTestdataValue(String testdataObjectName, String testdataObjectKey){
		String pageName = PageManager.getPage().getName();
		String env = environment();
		var pageData = loadPageData(pageName);
		Map <String,String> testdata = pageData.getTestdata(env, testdataObjectName);
		if (testdata.isEmpty()) {
			env = DEFAULT;
			testdata = pageData.getTestdata(env, testdataObjectName);
		}
		if (testdata.isEmpty()) {
			var errorMessage = SentinelStringUtils.format("Testdata {} could not be found for the {} environment in {}.yml", testdataObjectName, env, pageName);
			throw new FileException(errorMessage, new File(pageName + ".yml"));
		}
		String data = testdata.get(testdataObjectKey);
		if (data == null) {
			var errorMessage = SentinelStringUtils.format("Data for {} key could not be found in {} for the {} environment in {}.yml", testdataObjectKey, testdataObjectName, env, pageName);
			throw new FileException(errorMessage, new File(pageName + ".yml"));
		}
		log.debug("{} loaded for testdata object {} in {} environment from {}.yml: {}", testdataObjectKey, testdataObjectKey, env, pageName, data);
		return data;
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